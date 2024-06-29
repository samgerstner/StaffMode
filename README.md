# Staff Mode
Staff Mode is a Minecraft plugin that allows players to toggle staff permission groups on and off. The plugin also supports logging to a MySQL database. 

Please see below for installation instructions and required dependencies.

## Dependecies
Staff Mode requires several dependencies to function correctly including Vault, a supported permissions plugin, and a supported economy plugin. Any plugins that are supported by Vault will work, but we have supplied some recommendations below.

- **Vault**: https://www.spigotmc.org/resources/vault.34315/
- **LuckPerms**: https://www.spigotmc.org/resources/luckperms.28140/
- **EssentialsX**: https://www.spigotmc.org/resources/essentialsx.9089/

## Installation
1. Download the latest version of the plugin .jar file from the GitHub artifacts or our Spigot resource page (link coming soon).

2. Copy the .jar file into your server's `plugins` folder.

3. Restart or reload your server for changes to take effect.

4. Modify the config file located in `plugins/StaffMode/config.yml` to your liking.

## Command Usage and Permissions
The primary plugin command is `/staffmode`. Ther are 3 sub-commands that include:

- `/staffmode help` - Displays the command usage.
- `/staffmode enable <role>` - Enable the specified staff role/group.
- `/staffmode disbale <role>` - Disables the specified staff role group.

<br>
The plugin supports the following permission nodes:

- `staffmode.*` - Allows access to all roles/groups.
- `staffmode.<role>` - Allows access to a specific group (i.e. `'staffmode.admin`) [Please note that the group must exist in your permissions plugin for this to work].

## License
Staff Mode is licensed under the GNU Lesser General Public License Version 3. See the license.txt file for more information.