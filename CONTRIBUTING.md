## Issues

Along with a bug report, provide a functional example repository which
reproduces the bug you're experiencing. We've made this very easy by providing
a full-stack (React Native + Express.js) example app, which you can fork and
alter to reproduce your bug:

[ReactNativeBackgroundUploadExample](https://github.com/Vydia/ReactNativeBackgroundUploadExample)

## Development

The repo has two install configurations so that both AppFolio and non-AppFolio
contributors can build it.

**AppFolio members** — the default `.npmrc` routes through AppFolio's JFrog
mirror. Set `JFROG_ACCESS_TOKEN` in your environment (available from your JFrog
profile) and run:

```
yarn install
```

**Non-AppFolio contributors** — use the public npm registry:

```
cp .npmrc-public .npmrc
yarn install
```

CI uses the public configuration; publishing goes to GitHub Packages via
`GITHUB_TOKEN` and does not require JFrog access.
