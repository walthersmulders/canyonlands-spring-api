# Canyonlands Spring API

<div align="center">

[![Static Badge](https://img.shields.io/badge/license-GPL_v3.0-238636?style=for-the-badge)](COPYING)
![Static Badge](https://img.shields.io/badge/status-work_in_progress-orange?style=for-the-badge)
</div>

<div align="center">

![Static Badge](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![Static Badge](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![Static Badge](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Static Badge](https://img.shields.io/badge/Keycloak-blue?style=for-the-badge&logo=keycloak)

</div>

---

> [!IMPORTANT]  
> This project is a work in progress and might not perform as expected.

## Documentation

For full project documentation, guides, recipes, resources see the project website:

[Project Canyonlands](https://walthersmulders.com/personal-projects/canyonlands/getting-started/overview/)

## License

GNU General Public License v3.0 or later

See [COPYING](COPYING) to see the full text.

## Postman Collection

A Postman Collection and Environment export can be found in the [docs](docs/api) directory.

Keep in mind the Postman collection does not contain all the supported API endpoints at this 
stage. I will update the collection as progress on the project is made.

> [!NOTE] 
> For a full walkthrough of the API endpoints please refer to the project documentation found here:
> [Project Canyonlands](https://walthersmulders.com/personal-projects/canyonlands/getting-started/overview/)

## Breaking Changes

> [!CAUTION]
> You might have to perform data wipes on the database schema between commits. I will update the 
> base schema file until the v1 milestone has been reached (in terms of feature stability), at 
> which point the branch will be merged into `master` and actively tagged and managed from that 
> point onwards (meaning incremental schema changes will happen via liquibase).
