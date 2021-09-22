# Alcazar
Alcazar is a simple PvP utility plugin for Paper servers. It supports Minecraft 1.17.1 and up.

## Features
+ Advanced death messages
+ Inventory saving / loading
+ IRC-style WHOIS lookup
+ Configurable maximum inventory limit

## Installation
1. Download Alcazar from the [releases](https://github.com/oko366/Alcazar/releases) page.
2. Set up MySQL (or MariaDB), creating a database and table (`uuid VARCHAR(128), inventory_name VARCHAR(64) UNIQUE, si TEXT, sa TEXT`).
3. Configure generated config.yml with your jdbc url.
4. Done!

## Building
Alcazar does not require a patched server jar. Simply run `mvn clean install`.

## Licensing
This software is licensed under the terms of the MIT license.

You can find a copy of the license in the [LICENSE file](LICENSE).

## Credits
This plugin was inspired by [DevotedPVP](https://github.com/oko366/DevotedPvP), initially created by DevotedMC.
