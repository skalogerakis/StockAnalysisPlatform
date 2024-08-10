package Schemas

case class InputCSVSchema(ID: String, SecType: String, Date: String, Time: String, Ask: String, Ask_volume: String,
                          Bid: String, Bid_volume: String, Ask_time: String, Day_high_ask: String, Close: String,
                          Currency: String, Day_High_Ask_Time: String, Days_High: String, ISIN: String, Auction_Price: String,
                          Day_Low_Ask: String, Day_Low : String, Day_Low_Ask_Time: String, Open: String, Nominal_value: String,
                          Last: String, Last_volume: String, Trading_Time: String, Total_volume: String, Mid_price: String,
                          Trading_Date: String, Profit: String, Current_Price: String, Related_Indices: String, Day_High_Bid_Time: String,
                          Day_low_bid_time: String, Open_Time: String, Last_trade_time: String, Close_Time: String, Day_high_Time: String,
                          Day_low_Time: String, Bid_Time: String, Auction_Time: String
                         )

/*
0)ID*                   -> Unique ID
1)SecType*               -> Security Type (E)quity/(I)ndex
2)Date                  -> System date for last received update
3)Time                  -> System time for last received update
4)Ask                   -> Price of best ask order
5)Ask volume            -> Volume of best ask order
6)Bid                   -> Price of best bid order
7)Bid volume            -> Volume of best bid order
8)Ask time              -> Time of last ask
9)Day's high ask        -> Day's high ask
10)Close                -> Closing price
11)Currency             -> Currency (according to ISO 4217)
12)Day's high ask time  -> Day's high ask time
13)Day's high           -> Day's high
14)ISIN                 -> ISIN (International Securities Identification Number)
15)Auction price        -> Price at midday's auction
16)Day's low ask        -> Lowest ask price of the current day
17)Day's low            -> Lowest price of the current day
18)Day's low ask time   -> Time of lowest ask price of the current day
19)Open                 -> First price of current trading day
20)Nominal value        -> Nominal Value
21)Last*                 -> Last trade price
22)Last volume          -> Last trade volume
23)Trading time*         -> Time of last update (bid/ask/trade)
24)Total volume         -> Cumulative volume for current trading day
25)Mid price            -> Mid price (between bid and ask)
26)Trading date*         -> Date of last trade
27)Profit               -> Profit
28)Current price        -> Current price
29)Related indices      -> Related indices
30)Day high bid time    -> Days high bid time
31)Day low bid time     -> Days low bid time
32)Open Time            -> Time of open price
33)Last trade time      -> Time of last trade
34)Close Time           -> Time of closing price
35)Day high Time        -> Time of days high
36)Day low Time         -> Time of days low
37)Bid time             -> Time of last bid update
38)Auction Time         -> Time of last bid update,Time when last auction price was made
 */