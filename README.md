# Juery [![](https://img.shields.io/github/releaseMarthym/juery.svg)](https://GitHub.com/Marthym/juery/releases/) [![GitHub license](https://img.shields.io/github/license/Marthym/juery.svg)](https://github.com/Marthym/juery/blob/master/LICENSE)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Marthym_juery&metric=alert_status)](https://sonarcloud.io/dashboard?id=Marthym_juery)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Marthym_juery&metric=coverage)](https://sonarcloud.io/dashboard?id=Marthym_juery)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Marthym_juery&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=Marthym_juery)

**Juery** is a tiny Java library to manage search and filter query from user to database. **api** and **basic** packages 
have no dependencies. They contain everything you need to use Juery. The **jooq** package contains useful tools for 
projects using the jOOQ DSL.

## Installation

Use the package manager [maven](https://maven.apache.org/) to install juery.

```xml
<dependency>
    <groupId>fr.ght1pc9kc</groupId>
    <artifactId>juery-api</artifactId>
    <version>${juery.version}</version>
</dependency>
<dependency>
    <groupId>fr.ght1pc9kc</groupId>
    <artifactId>juery-basic</artifactId>
    <version>${juery.version}</version>
</dependency>
<dependency>
    <groupId>fr.ght1pc9kc</groupId>
    <artifactId>juery-jooq</artifactId>
    <version>${juery.version}</version>
</dependency>
```

## Usage

```java
import fr.ght1pc9kc.juery.api.Criteria;

Criteria.property("jedi").eq("Obiwan")
    .and(Criteria.property("age").gt(40)
    .or(Criteria.property("age").lt(20)));
```

```java
import fr.ght1pc9kc.juery.api.PageRequest;
import fr.ght1pc9kc.juery.api.pagination.Direction;
import fr.ght1pc9kc.juery.api.pagination.Order;
import fr.ght1pc9kc.juery.api.pagination.Sort;

PageRequest.builder()
    .page(2).size(100)
    .filter(Criteria.property("profile").eq("jedi").and(Criteria.property("job").eq("master")))
    .sort(Sort.of(new Order(Direction.ASC, "name"), new Order(Direction.DESC, "email")))
    .build();
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
