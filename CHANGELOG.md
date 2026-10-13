# Changelog

## 0.1.0 (2026-10-13)


### ⚠ BREAKING CHANGES

* **core:** NO

### Features

* add configuration for GraalVM and PostgreSQL ([9ceb1dc](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/9ceb1dc97f5bbc8e50fa7a504b4c0b77c990407f))
* add DockerCompose PostgreSQL ([d4893c6](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/d4893c6f8ecccfe2dfef267c2276953c0cdc145f))
* add Quarkus Native support with GraalVM ([caa0f53](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/caa0f53f2bf571a91ef365149a4c3e984a16ffa6))
* add support for Spring Boot Native with GraalVM ([2a70c71](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/2a70c71184f3c91c199be3f46b8dfc8cf51d0746))
* add support for Spring WebFlux Native with GraalVM ([fd43c21](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/fd43c218bdc49e7e3d036dc66dcac540846652c3))
* **core:** add cart items - products entities - repository with panache and own resources controller ([494a861](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/494a861f80755f4ce4ebd6ded277a8a2b53ed4ce))
* **core:** add Cart items and products ([99ef626](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/99ef6267b249ac41b67823e8d4f7e5d8288336f5))
* **core:** add CartItemsController with Flux and Mono, R2DBC repository, and data migration with Flyway ([af6e4b7](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/af6e4b779bb5dd362a9cfd347caeb2b8f7fe0d57))
* **core:** add reactive CartResource, CartService, and CartRepository ([b599fc4](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/b599fc4b528978f9f5ca778fe2fb93329c57f98f))
* **core:** add reactive CartResource, CartService, and CartRepository ([8cb15d7](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/8cb15d73ac17d8e69f26a6da11ce92debc076a3d))
* Init project quarkus reactive native ([d91e7ef](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/d91e7ef58d1ccd234ee5e5cca069e1a06591205a))
* init project spring webflux native ([e393ef7](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/e393ef778520b4df06bdc277346d8378b59e95cc))
* init proyect quarkus native ([c8e4666](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/c8e4666d264bbbad5a9e5bc6f22d9d983ee50ead))


### Bug Fixes

* migraciones Flyway en spring-webflux-native y PUT /carts sin items en quarkus-native ([4ca9f93](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/4ca9f93c5499e35a1194b93e4c96f058ce4031c2))
* necesary configuration in build for native execution ([65866dc](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/65866dcadad521d3c351f9a3eb657887d5a49c04))
* **quarkus-native:** PUT /carts/{id} sin items ya no responde 500 ([21b3499](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/21b34990d991ee463710a454a091f2de9ffd7824))
* **spring-webflux-native:** ejecutar migraciones Flyway en la app R2DBC ([a1bc01f](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/a1bc01fe9464c17bfde92f2b8b3559991093a1a9))


### CI / Dependencies

* add OpenSSF Scorecard, weekly CodeQL and release-please releases ([#9](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/issues/9)) ([0573503](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/057350382b90aaad7cc940eb634814ef6cb58b2b))
* usar reusable-workflows v1.0.0 y endurecer el workflow nativo ([95413c6](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/95413c67b22bb17e69d26123e14d3f5f7cf4b02b))
* use AZapata27/reusable-workflows v1.0.0 and harden native workflow ([b4d87b7](https://github.com/AZapata27/graalvm-spring-quarkus-benchmark-lab/commit/b4d87b7fa72df317777d0f4f250eecbf734f2287))
