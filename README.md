# fee-toolkit

A shared fee/interest calculation library published as a Spring Boot starter.
Consumer teams across the firm add `com.acme:fee-toolkit:1.4.0` as a dependency
and get an auto-configured `FeeCalculator`, `FeeSchedule`, and `FeeCache`.

## You are the maintainer
You own this library. **Other teams already run version 1.4.0 in production.**
Any change you make must keep existing consumers working.

## Run
- Build & test:  `mvn clean test`
- Java 21 required.

## Config (consumers set this in their own application.yml)
```yaml
acme:
  fees:
    rounding-mode: HALF_EVEN
    scale: 2
    tiers:
      - up-to-amount: 1000
        rate: 0.020
      - up-to-amount: 10000
        rate: 0.015
```

## During the session you may be asked to:
1. Add a feature (see interviewer).
2. Review a teammate's AI-generated change as a pull request.
3. Write or critique a test.

You are encouraged to use any AI assistant. Narrate your thinking; you own every
line you submit and will be asked to defend it.
