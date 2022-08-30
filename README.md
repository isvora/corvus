# Corvus

Finding upcoming trending tickers on stocktwits

Stocktwits only shows the first 10 trending symbols on their webpage.
Through some digging we can see that they actually have an internal undocumented API that is able to retrieve more
than only the first 10 symbols you see on the webpage.

## Database

![](doc/images/database.png)

## Flow

We have a scheduled method that runs every minute scanning for the tickers that are currently trending between
ranks 11 and 20. These are not known by the public who just use the stocktwits website.

```
@Scheduled(cron = "*/60 * * * * *")
public void scanUpcomingTickers() {
    ...
}
```

We can do that by leveraging the undocumented API for stocktwits like this:

```
curl --location --request GET 'https://api-gw-prd.stocktwits.com/rankings/api/v1/rankings?identifier=ALL&identifier-type=exchange-set&top=20&type=ts' \
--header 'Authorization: Basic SECRET_KEY'
```

This will return the top 20 trending tickers, from which we can remove the first 10 that already are present on the website.

What we do now is storing the symbols in the Database in order to be able to compare them later.

Everytime we run the scheduled method we compare the data we get with what we have in the database. By this we can determine the follow actions:

• A new ticker entered the top 20 ranking, so we can mark it accordingly.

• A ticker either went up or down in ranking, so we can mark it accordingly

There is a rest endpoint to gather these tickers at `/upcoming-tickers`

## Discord

For easier use I've built `Bender`, a Discord bot to help with visualizing the data.

### upcoming-tickers

The scanning happens every  minute, and we publish any new data on Discord.

An example with how this looks can be seen here:

```
⬆️ 2. $BCRX https://stocktwits.com/symbol/BCRX Rank: 12, Price: 13.95$, Price Change: -2.17% 📉 
⬆️ 3. $AMZN https://stocktwits.com/symbol/AMZN Rank: 13, Price: 129.79$, Price Change: -0.73% 📉 
⬆️ 4. $BRISE.X https://stocktwits.com/symbol/BRISE.X Rank: 14, Price: 0.0$, Price Change: 6.3% 📈 
⬆️ 5. $EDU https://stocktwits.com/symbol/EDU Rank: 15, Price: 28.35$, Price Change: 0.71% 📈 
🆕 6. $BMO https://stocktwits.com/symbol/BMO Rank: 16, Price: 98.08$, Price Change: -0.41% 📉 
⬆️ 7. $SOXL https://stocktwits.com/symbol/SOXL Rank: 17, Price: 15.05$, Price Change: -5.82% 📉 
⬇️ 8. $QQQ https://stocktwits.com/symbol/QQQ Rank: 18, Price: 304.41$, Price Change: -0.99% 📉 
⬇️ 9. $BBY https://stocktwits.com/symbol/BBY Rank: 19, Price: 73.7$, Price Change: -0.61% 📉 
⬇️ 10. $WEBR https://stocktwits.com/symbol/WEBR Rank: 20, Price: 9.86$, Price Change: 5.57% 📈 
--------------------------------------------------
⬆️ 4. $BMO https://stocktwits.com/symbol/BMO Rank: 14, Price: 98.08$, Price Change: -0.41% 📉 
⬇️ 5. $BRISE.X https://stocktwits.com/symbol/BRISE.X Rank: 15, Price: 0.0$, Price Change: 5.93% 📈 
🆕 6. $SGMO https://stocktwits.com/symbol/SGMO Rank: 16, Price: 5.25$, Price Change: -2.78% 📉 
⬇️ 7. $EDU https://stocktwits.com/symbol/EDU Rank: 17, Price: 28.35$, Price Change: 0.71% 📈 
⬇️ 8. $SOXL https://stocktwits.com/symbol/SOXL Rank: 18, Price: 15.05$, Price Change: -5.82% 📉 
--------------------------------------------------
```

### /tickers

The `/tickers` command is used to retrieve the current 10 symbols between rank 11 and rank 20.

Example: `/tickers`

Response:

```
1. $SPY https://stocktwits.com/symbol/SPY Rank: 11, Price: 402.63$, Price Change: -0.66% 📉 
2. $BRISE.X https://stocktwits.com/symbol/BRISE.X Rank: 12, Price: 0.0$, Price Change: 4.74% 📈 
3. $MMM https://stocktwits.com/symbol/MMM Rank: 13, Price: 126.44$, Price Change: -2.09% 📉 
4. $BMO https://stocktwits.com/symbol/BMO Rank: 14, Price: 98.08$, Price Change: -0.41% 📉 
5. $PLAB https://stocktwits.com/symbol/PLAB Rank: 15, Price: 21.77$, Price Change: -1.89% 📉 
6. $LMND https://stocktwits.com/symbol/LMND Rank: 16, Price: 21.71$, Price Change: -2.82% 📉 
7. $AMZN https://stocktwits.com/symbol/AMZN Rank: 17, Price: 129.79$, Price Change: -0.73% 📉 
8. $SGMO https://stocktwits.com/symbol/SGMO Rank: 18, Price: 5.25$, Price Change: -2.78% 📉 
9. $BBY https://stocktwits.com/symbol/BBY Rank: 19, Price: 73.7$, Price Change: -0.61% 📉 
10. $BCRX https://stocktwits.com/symbol/BCRX Rank: 20, Price: 13.95$, Price Change: -2.17% 📉 

```

### /comments

The `/comments [ticker]` command is used to get the sentiment for the last 30 comments for a given ticker.
30 is the maximum number Stocktwits api can retrieve.

Parameter: `ticker` symbol for the company you want to retrieve the comment sentiment. Example: TSLA, SPY, MSFT etc.

Example: `/comments SPY`

Response:
```
Sentiment for $SPY
8 Bullish 📈 
9 Bearish 📉 
13 Neutral 📊
```
