Github scanner mvc
===

## Description:

> This application was developed for use during the recruitment process.

Application designed to scan GitHub repositories (repository name, owner login) and
its branches (branch name, last commit hash sha).

## API Endpoints

* GET localhost:8080/api/v1/user/{ownerName}/repos - get repositories and its branches by providing username

Example 200 response:

```json
[
    {
        "name": "BD",
        "ownerLogin": "Blaszak",
        "branches": [
            {
                "name": "master",
                "lastCommitSha": "30ea38924e81c06c6327314c66c43348186ce29f"
            }
        ],
        "fork": false
    }
]
```

Example 404 not found response:

```json
{
    "message": "404 Not Found on GET request for \"https://api.github.com/users/Gienio454555454/repos\": \"{\"message\":\"Not Found\",\"documentation_url\":\"https://docs.github.com/rest/repos/repos#list-repositories-for-a-user\",\"status\":\"404\"}\"",
    "status": 404
}
```

## API Clients:

* GithubApiClient
    * Contract
      *  public @Nonnull Set<GitRemoteRepositoryDetails> getRepositories(@Nonnull String ownerName)


## New application properties:

* github.api.base-url=https://api.github.com - give here a github backend API to fetch repositories/branches etc
* github.token= - to avoid API anonymous usage quota (60 requests per hour), provide the token.