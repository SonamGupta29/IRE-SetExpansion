Name of Project : Set Expansion

Link to github repository - https://github.com/mrugani/IRE-SetExpansion 

Explanation of code

The entry point for the project is the Runner.java class which takes the following arguments

Arguments

1. Number of output results
2. Name of input file

Once the program is run it first creates a primary index file for the Googl dataset. This primary index file contains the word along with its vector. There are around three million words for which the vectors are stored. As this file is very large so in order to enable fast access in this file we create a secondary index file containing the word along with its offset in the primary index file. 

The words read from the input file are stored in the array. Now we give each word as input to various search engines like bing, google, wikipedia etc and fetch the relevant web pages. Now these web pages are tokenised with the help of JSOUP Parser. Each of the word obtained from the above process is checked if it is not a stop word and then the cosine distance of these words is calculated from each of the words in the input terms array.
Cosine distance is defined as the dot product of the vectors of the two words. For finding the vector  for a word we search for that word in the primary index file with the help of secondary index file.

Once the cosine distance for all words have been found we chosse the top 'n' words which are closest to the input words and they are displayed.

    

