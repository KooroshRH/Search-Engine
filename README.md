# Search Engine
This project comporises `Indexer`, `Tokenizer`, `QueryProcessor` parts. Also, it uses a helper code named `FileWorker` for loading dataset and saving checkpoints for indexer and tokenizer sections.  
## Method
In `QueryProcessor` side, we use **TF-IDF** algorithm for processing every user's query. Also, for determining the similarities between the user query and each document's representation, we use **Cosine** similarity function in vector space.  
> NOTE: This project's data preprocessing and augmentation parts are based on persian language.
## How it works?
To run this search engine, we have to run `main` file. First, `tokenizer` and `indexer` instances will be created. After that and with initializing the `fileWorker` instance, we can load dataset with either `fileIndex` or `labeledFileIndex` function from `fileWorker` class.  
In the end, after some preprocessings, we define the `queryProcessor` instance with passing the `indexer` and the `tokenizer` to it's constructor. We can write our queries in terminal with calling the `startListening` function.
## Resources
#### Dependencies (JAR format)
- [commons-collections4-4.1.jar](https://github.com/KoroshRH/Search-Engine/raw/master/resources/commons-collections4-4.1.jar)
- [poi-3.17.jar](https://github.com/KoroshRH/Search-Engine/raw/master/resources/poi-3.17.jar)
- [poi-ooxml-3.17.jar](https://github.com/KoroshRH/Search-Engine/raw/master/resources/poi-ooxml-3.17.jar)
- [poi-ooxml-schemas-3.17.jar](https://github.com/KoroshRH/Search-Engine/raw/master/resources/poi-ooxml-schemas-3.17.jar)
- [xmlbeans-2.6.0.jar](https://github.com/KoroshRH/Search-Engine/raw/master/resources/xmlbeans-2.6.0.jar)
#### Datasets
- [IR00_3_11k News.xlsx](https://github.com/KoroshRH/Search-Engine/raw/master/resources/IR00_3_11k%20News.xlsx)
- [IR00_3_20k News.xlsx](https://github.com/KoroshRH/Search-Engine/raw/master/resources/IR00_3_20k%20News.xlsx)
