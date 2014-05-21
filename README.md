Android-App: Stock Helper (CoolStockTool)
============

## Demo video:
https://www.youtube.com/watch?v=tR6mGoPRgAo&feature=youtu.be

## Motication:  
- People want to make sure that the information about stock is not only updated and but reliable.
- People might hope to have some straight-forward predicted price, which has alrealy been caculated by some complex algorithm.

## Use Case:
- People can cath up the most updated not only information but prediction every moment by simply clicking a button.
- People are able get exprienced stock stock traders's advice by going to the forum of each stock.

## Functions
- Chat room for each topic within each stock
- Voting result for each message
- BookMarked favorate stocks
- Log in credential
- Updated information and Predicted stock price

## Implementation:
- Retreive updated Stock Info(Yahoo Stock API)
- Store user's info and activity in back-end server(AWS)
- Implement some prediction Algorithm(polynomial Regression)
- Integrate back-end function into Android development(multi-thread)

## Future Work
- Support more Prediction Algorithm
- Support Facebook account Log-in
- Implement some animation decorating UI
- Accelerate Server data computation

## How to run it
Simply clone the project to local repository. Enter AWS credential including Secret Key and Access Key in ```AwsCredentials.properties```, before run it on simulator or any Android device. 

