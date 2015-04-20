# IRE-SetExpansion
Course: Information Retrieval and Extraction

Project Name: Set Expansion

Details: The repository is created for IRE Major Project -- Set Expansion

Link for the presentation: https://docs.google.com/presentation/d/15IVqeBkn7UEUgHF6Zsbf_dmbJpqkOWtw6-oKHLOA7bw/edit?usp=sharing

Link for Slides: http://www.slideshare.net/anirudhalampally/ire-set-expansion

Setup Details:
1. This project uses pre-trained dataset from Google news. This can be replaced by any other trained dataset using word2vec.

2. Use the python scripts from the folder "scripts" to generate primary and secondary indexes.

3. After generating index, copy the index to the current working directory.

4. Run the class Runner.java from the package concept. Provide two argument:
    <Number of results>, <Input file containing seedlist>
   e.g. java Runner 10 Input

5. The Runner class will fetch the matching webpages containing input seed list from Google, Bing, Wikipedia, Faroo, DuckDuckGo etc using their web search APIs. For each of the token obtained from the webpage, it finds the similarity will all the seed terms using word2vec vectors. Additionally, it also finds <ol>, <li>, <table>, <select> patterns from the webpage containing seed terms. 

6. Top results are selected based on similarity.
