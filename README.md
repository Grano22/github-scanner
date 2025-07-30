Github scanner mvc
===

## Description:

> This application was developed for use during the recruitment process.

Application designed to scan GitHub repositories (repository name, owner login) and
its branches (branch name, last commit hash sha).

## API Clients:

* GithubApiClient
    * Contract
      *  public @Nonnull Set<GitRemoteRepositoryDetails> getRepositories(@Nonnull String ownerName)


## New application properties:

* github.api.base-url=https://api.github.com - give here a github backend API to fetch repositories/branches etc
* github.token= - to avoid API anonymous usage quota (60 requests per hour), provide the token.