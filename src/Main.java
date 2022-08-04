import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        Tokenizer tokenizer = new Tokenizer();
        Indexer indexer = new Indexer(tokenizer);
        FileWorker fileWorker = new FileWorker(indexer);
        System.out.println("Indexing...");
        try {
            fileWorker.fileIndex("C:\\Users\\Korosh\\IdeaProjects\\IR-project\\IR_Spring2021_ph12_7k.xlsx");
//            fileWorker.labeledFileIndex("C:\\Users\\Korosh\\IdeaProjects\\IR-project\\IR00_3_11k News.xlsx");
//            fileWorker.labeledFileIndex("C:\\Users\\Korosh\\IdeaProjects\\IR-project\\IR00_3_17k News.xlsx");
//            fileWorker.labeledFileIndex("C:\\Users\\Korosh\\IdeaProjects\\IR-project\\IR00_3_20k News.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }
        indexer.createChampionsList();
        indexer.removeFrequentTerms();
        QueryProcessor queryProcessor = new QueryProcessor(indexer, tokenizer);
        System.out.println("Indexed terms : " + indexer.getIndexSize());
        queryProcessor.startListening();
    }
}
