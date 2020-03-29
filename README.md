# Most Starred Github Repos Project

 Small app that lists the most starred Github repos that were created in the last 30 days. 
Fetching the sorted JSON data directly from the Github API.

## Features

- Listing the most starred Github repos that were created in the last 30 days.
- The results are displayed as a list. One repository per row.
- For each repo/row have the following details 👍 
    1. - Repository name
    2. - Repository description
    3. - Numbers of stars for the repo.
    4. - Username and avatar of the owner.
- Pagination support through endless scrolling, fetching new JSON data (scrolling both up and down)

we get the data by calling the following endpoint 
https://api.github.com/search/repositories?q=created:>2017-10-22&sort=stars&order=desc

![Screenshot_20200328-151159_Most Starred Github Repos](https://user-images.githubusercontent.com/11277981/77826445-abdc2180-710f-11ea-82f2-38106facc34a.jpg)
![Screenshot_20200328-151234_Most Starred Github Repos](https://user-images.githubusercontent.com/11277981/77826447-ad0d4e80-710f-11ea-9ad9-29d5c2843cd1.jpg)

## Built With
* [Android Studio] using java


## Authors

* **Walid CHEKKOURI**

## Acknowledgments

* Hat tip to anyone who would like to contribute to the project
* Inspiration
* etc
