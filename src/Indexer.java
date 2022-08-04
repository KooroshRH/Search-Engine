import com.sun.source.tree.Tree;

import java.util.*;

/*
    we removing frequent terms with a ratio; if each term has more frequencies of this ratio of doc count will delete
 */
public class Indexer
{
    private TreeMap<String, TreeMap<String, HashMap<Integer, Integer>>> index;
    private TreeMap<String, TreeMap<String, HashMap<Integer, Integer>>> championsList;
    private Tokenizer tokenizer;
    private HashMap<Integer, String> docLink;
    private HashMap<Integer, Integer> docLength;
    private String searchCategory = "all";

    private final float frequentRatio = 0.05f;
    private final int CHAMPIONS_LIST_SIZE = 20;

    public Indexer(Tokenizer tokenizer)
    {
        this.tokenizer = tokenizer;
        index = new TreeMap<>();
        championsList = new TreeMap<>();
        docLink = new HashMap<>();
        docLength = new HashMap<>();
    }

    public void addToIndex(String data, int docId, String link, String category)
    {
        List<String> tokens = tokenizer.tokenize(data);
        if (!docLink.containsKey(docId))
        {
            docLink.put(docId, link);
        }
        docLength.put(docId, docLength.getOrDefault(docId, 0) + tokens.size());
        for (String token : tokens) {
            if (index.containsKey(category))
            {
                if (index.get(category).containsKey(token))
                {
                    index.get(category).get(token).put(docId, index.get(category).get(token).getOrDefault(docId, 0) + 1);
                }
                else
                {
                    HashMap<Integer, Integer> tempList = new HashMap<>();
                    tempList.put(docId, 1);
                    index.get(category).put(token, tempList);
                }
            }
            else
            {
                TreeMap<String, HashMap<Integer, Integer>> categoryIndex = new TreeMap<>();
                HashMap<Integer, Integer> tempList = new HashMap<>();
                tempList.put(docId, 1);
                categoryIndex.put(token, tempList);
                index.put(category, categoryIndex);
            }
        }
    }

    public void createChampionsList()
    {
        for (String category : index.keySet())
        {
            TreeMap<String, HashMap<Integer, Integer>> temp = new TreeMap<>();
            for (String term : index.get(searchCategory).keySet())
            {
                temp.put(term, sortByFreq(index.get(searchCategory).get(term)));
            }
            championsList.put(category, temp);
        }
    }

    public HashMap<Integer, Integer> getPostingsList(String term)
    {
        return index.get(searchCategory).getOrDefault(term, new HashMap<>());
    }

    public HashMap<Integer, Integer> getChampionsList(String term)
    {
        return championsList.get(searchCategory).getOrDefault(term, new HashMap<>());
    }

    public void setSearchCategory(String searchCategory) {
        this.searchCategory = searchCategory;
    }

    public String getLink(int docId)
    {
        return docLink.get(docId);
    }

    public int getIndexSize()
    {
        int size = 0;
        for (String category : index.keySet())
        {
            size += index.get(category).size();
        }
        return size;
    }

    public int getDocCount()
    {
        return docLink.keySet().size();
    }

    public HashMap<Integer, Integer> getDocLength() {
        return docLength;
    }

    private HashMap<Integer, Integer> sortByFreq(HashMap<Integer, Integer> input)
    {
        List<Map.Entry<Integer, Integer>> list = new LinkedList<>(input.entrySet());

        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        HashMap<Integer, Integer> temp = new HashMap<>();
        int count = 0;
        for (Map.Entry<Integer, Integer> aa : list)
        {
            count++;
            temp.put(aa.getKey(), aa.getValue());
            if (count == CHAMPIONS_LIST_SIZE)
            {
                break;
            }
        }
        return temp;
    }

    public void removeFrequentTerms()
    {
        for (String category : index.keySet())
        {
            List<String> temp = sortByValue(index.get(category));
            for (String term : temp)
            {
                if (index.get(category).get(term).size() > (int)(frequentRatio*docLink.keySet().size()))
                {
                    index.get(category).remove(term);
                }
            }
        }
    }

    public List<String> sortByValue(TreeMap<String, HashMap<Integer, Integer>> hm)
    {
        List<Map.Entry<String, HashMap<Integer, Integer>>> list = new LinkedList<>(hm.entrySet());

        list.sort((o1, o2) -> Integer.compare(o1.getValue().size(), o2.getValue().size()) * -1);

        List<String> temp = new ArrayList<>();
        for (Map.Entry<String, HashMap<Integer, Integer>> aa : list)
        {
            temp.add(aa.getKey());
        }
        return temp;
    }
}
