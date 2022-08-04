# Search Engine
This project comporises `Indexer`, `Tokenizer`, `QueryProcessor` parts. Also, it uses a helper code named `FileWorker` for loading dataset and saving checkpoints for indexer and tokenizer sections.  
In `QueryProcessor` side, we use **TF-IDF** algorithm for processing every user's query. Also, for determining the similarities between the user query and each document's representation, we use **Cosine** similarity function.  
> NOTE: This project's data preprocessing and augmentation parts are based on persian language.
