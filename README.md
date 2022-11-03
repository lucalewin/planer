# Planer (deprecated)

This android app is a (better) alternative to the substitution planer my school uses (Iserv).

## Deprecation reason: Iserv authentication

In the summer holidays Iserv changed how the login/authentication + cookie system works. Therefore this app is currently not able to authenticate and subsequently not able to request the substitution plans.

But since I don't have the time to continue working on this project and I graduate next year and subsequently don't need this app anymore, I decided to archive this repo.

## Motivation

I started this project because I did not like how complicated the substitution planer in our school was. We had to wait 60 seconds to view the plan of the next day which was (and still is) really annoying.

## How it works

This app basically automates the login process and then requests the plan for every day.
In the next step the app extracts the relevant information (webscraping) from the pages and then displays it to the user
