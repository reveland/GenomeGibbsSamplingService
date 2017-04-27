# GenomeGibbsSamplingService
Provide a Rest service for Gibbs sampling algorithm.

## Build
```
mvn clean install
```

## Run
```
mvn spring-boot:run
```

## Endpoint
| Methode | endpoint    | params | purpose |
| --------|---------|-------|--|
| GET  | /start   | -    | start the algorithm |
| GET | /stop | -    | stop the algorithm |
| GET  | /tree   | -    | get the tree of the running algorithm |
| GET | /genome | refGenomeName, genomeName1, genomeName2    | get 3 genome representation (see data structure bellow) |
| GET  | /sample   | refGenomeName, genomeName    | get one genome representation |
### examples:
- http://localhost:8080/start
- http://localhost:8080/sample?refGenomeName=hg18&genomeName=rheMac2
- http://localhost:8080/genome?refGenomeName=canFam&genomeName1=rheMac&genomeName2=bosTau

## Genome Comparer json data structure
```
{
  "blocks": [
    {
      "id": "0",
      "referenceChromosomeLabel": "1",
      "size": 3
    },
    {
      "id": "1",
      "referenceChromosomeLabel": "1",
      "size": 5
    },
    {
      "id": "2",
      "referenceChromosomeLabel": "1",
      "size": 2
    },
    {
      "id": "3",
      "referenceChromosomeLabel": "2",
      "size": 4
    },
    {
      "id": "4",
      "referenceChromosomeLabel": "2",
      "size": 5
    }
  ],
  "genomes": [
    {
      "name": "referenceGenom",
      "chromosomes": [
        {
          "label": "1",
          "blocks": [
            {
              "id": "0",
              "inverted": false
            },
            {
              "id": "1",
              "inverted": false
            },
            {
              "id": "2",
              "inverted": false
            }
          ]
        },
        {
          "label": "2",
          "blocks": [
            {
              "id": "3",
              "inverted": false
            },
            {
              "id": "4",
              "inverted": false
            }
          ]
        }
      ]
    },
    {
      "name": "genomeName1",
      "chromosomes": [
        {
          "label": "1",
          "blocks": [
            {
              "id": "1",
              "inverted": false
            },
            {
              "id": "3",
              "inverted": true
            },
            {
              "id": "4",
              "inverted": false
            }
          ]
        },
        {
          "label": "2",
          "blocks": [
            {
              "id": "2",
              "inverted": true
            },
            {
              "id": "0",
              "inverted": false
            }
          ]
        }
      ]
    },
    {
      "name": "genomeName2",
      "chromosomes": [
        {
          "label": "1",
          "blocks": [
            {
              "id": "2",
              "inverted": true
            },
            {
              "id": "4",
              "inverted": false
            }
          ]
        },
        {
          "label": "2",
          "blocks": [
            {
              "id": "3",
              "inverted": false
            },
            {
              "id": "0",
              "inverted": true
            },
            {
              "id": "1",
              "inverted": false
            }
          ]
        }
      ]
    }
  ]
}
```
