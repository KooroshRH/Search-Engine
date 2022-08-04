import java.util.*;

/*
    For solving the problem of loss in normalization, we also tokenize the query and then search for the results
 */
public class QueryProcessor
{
    private Indexer indexer;
    private Tokenizer tokenizer;

    private final int MAX_RESULT = 10;
    private final boolean IS_CHAMPIONS_LIST_ENABLED = true;
    private final boolean IS_USING_MAX_HEAP = true;

    public QueryProcessor(Indexer indexer, Tokenizer tokenizer)
    {
        this.tokenizer = tokenizer;
        this.indexer = indexer;
    }

    public void startListening()
    {
        Scanner scanner = new Scanner(System.in);
        while(true)
        {
            System.out.print("Ready for your queries : ");
            String query = scanner.nextLine();
            String category = "all";
            if (query.startsWith("cat"))
            {
                category = query.split(":")[1].split(" ")[0];
                query = query.replace("cat:" + category + " ", "");
            }
            indexer.setSearchCategory(category);
            printResults(processQuery(query));
        }
    }

    private double calculateTfidf(String term, int docId)
    {
        double tf = indexer.getPostingsList(term).containsKey(docId) ? (1 + Math.log(indexer.getPostingsList(term).get(docId))) : 0;
        double idf = indexer.getPostingsList(term).size() > 0 ? Math.log((float)indexer.getDocCount() / indexer.getPostingsList(term).size()) : 0;
        return tf * idf;
    }

    private double calculateQueryTfidf(String term, List<String> query)
    {
        int count = 0;
        for (String token : query)
        {
            if (term.equals(token)) count++;
        }
        double tf = count != 0 ? (1 + Math.log(count)) : 0;
        double idf = indexer.getPostingsList(term).size() > 0 ? Math.log((float)indexer.getDocCount() / indexer.getPostingsList(term).size()) : 0;
        return tf * idf;
    }

    public List<String> processQuery(String query)
    {
        List<String> results = new ArrayList<>();
        List<String> queryTokens = tokenizer.tokenize(query);
        HashMap<Integer, Double> score = new HashMap<>();
        for (String queryToken : queryTokens) // the first idea for preventing of loss in some normalizing is to also tokenize queries like the docs
        {
            double queryWeight = calculateQueryTfidf(queryToken, queryTokens);
            if (IS_CHAMPIONS_LIST_ENABLED)
            {
                for (Integer docId : indexer.getChampionsList(queryToken.trim()).keySet())
                {
                    score.put(docId, score.getOrDefault(docId, 0d) + (queryWeight * calculateTfidf(queryToken, docId)));
                }
            }
            else
            {
                for (Integer docId : indexer.getPostingsList(queryToken.trim()).keySet())
                {
                    score.put(docId, score.getOrDefault(docId, 0d) + (queryWeight * calculateTfidf(queryToken, docId)));
                }
            }
        }
        score.replaceAll((i, v) -> score.get(i) / indexer.getDocLength().get(i));
        if (IS_USING_MAX_HEAP)
        {
            HashMap<Integer, Double> finalScore = score;
            PriorityQueue<Integer> maxHeap = new PriorityQueue<>((o1, o2) -> finalScore.get(o2).compareTo(finalScore.get(o1)));
            maxHeap.addAll(score.keySet());
            for (int i = 0; i < MAX_RESULT; i++)
            {
                if (maxHeap.size() > 0)
                {
                    results.add(indexer.getLink(maxHeap.poll()));
                }
                else
                {
                    break;
                }
            }
        }
        else
        {
            score = sortByValue(score);
            int count = 0;
            for (int docId : score.keySet())
            {
                count++;
                results.add(indexer.getLink(docId));
                if (count == MAX_RESULT)
                {
                    break;
                }
            }
        }
        return results;
    }

    public HashMap<Integer, Double> sortByValue(HashMap<Integer, Double> hm)
    {
        List<Map.Entry<Integer, Double>> list = new LinkedList<>(hm.entrySet());

        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        HashMap<Integer, Double> temp = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> aa : list)
        {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    private void printResults(List<String> results)
    {
        for (String link : results)
        {
            System.out.println(link);
        }
    }
}
