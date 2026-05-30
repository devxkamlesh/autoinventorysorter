# Auto Inventory Sorter

<p align="center">
  <img src="logo.png" alt="Auto Inventory Sorter Logo" width="200"/>
</p>

<p align="center">
  <strong>Smart inventory management for Minecraft</strong>
</p>

<p align="center">
  
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.8-green.svg)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Mod%20Loader-Fabric-blue.svg)](https://fabricmc.net/)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

</p>

A powerful client-side Minecraft mod that automatically organizes your inventory with intelligent sorting, hotbar management, and quality-of-life features.

## ✨ Features

### 🎯 Core Features
- **One-Key Sorting** - Press `R` to instantly organize your inventory
- **Smart Categorization** - Items sorted by type, rarity, name, quantity, or durability
- **Auto-Stack Merging** - Automatically combines partial stacks
- **Chest Sorting** - Sort any container with the same key

### 🔒 Hotbar Slot Locking
- Lock specific hotbar slots to always contain certain item types
- Default locks: Sword (slot 1), Pickaxe (slot 2), Axe (slot 3), Totem (slot 9)
- Fully customizable via config
- Items automatically move to locked slots during sorting

### 🔄 Auto-Refill System
- Automatically refills consumed items from your inventory
- Supports: Food, Blocks, Torches, Arrows
- Monitors hotbar every 0.25 seconds
- Smart stack merging

### 🚫 Blacklist System
- Exclude specific items from sorting
- Exclude specific slots from sorting
- Preserve custom item arrangements
- Perfect for maps, compasses, and special items

### 📦 Additional Features
- Auto-sort on inventory open (optional)
- Move valuable items to top
- Clean junk items automatically
- Profile system (Survival, PvP, Mining, Building)
- Multiplayer compatible (client-side only)

## 📥 Installation

### Requirements
- **Minecraft**: 1.21.8
- **Fabric Loader**: 0.16.9+
- **Fabric API**: 0.136.1+1.21.8
- **Java**: 21+

### Optional Dependencies
- **Mod Menu** - For in-game configuration
- **Cloth Config** - For advanced settings UI

### Steps
1. Install [Fabric Loader](https://fabricmc.net/use/)
2. Download [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download Auto Inventory Sorter from [Releases](https://github.com/devxkamlesh/autoinventorysorter/releases)
4. Place both JARs in `.minecraft/mods/` folder
5. Launch Minecraft!

## 🎮 Usage

### Basic Sorting
- Press **R** key to sort your inventory
- Press **R** in a chest to sort the chest
- Hotbar organizes automatically every 2 seconds

### Configuration
Edit `.minecraft/config/autoinventorysorter.json`:

```json
{
  "enabled": true,
  "sortMode": "ITEM_TYPE",
  "enableSlotLocking": true,
  "autoRefillHotbar": true,
  "enableChestSorting": true,
  "enableBlacklist": true
}
```

### Slot Locking
```json
{
  "lockSword": true,
  "swordSlot": 0,
  "lockPickaxe": true,
  "pickaxeSlot": 1
}
```

### Auto-Refill
```json
{
  "autoRefillHotbar": true,
  "refillFood": true,
  "refillBlocks": true,
  "refillTorches": true,
  "refillArrows": true
}
```

### Blacklist
```json
{
  "blacklistedItems": [
    "minecraft:compass",
    "minecraft:map"
  ],
  "blacklistedSlots": [10, 11, 12]
}
```

## 📚 Documentation

- **[FEATURES.md](FEATURES.md)** - Detailed feature documentation
- **[INSTALL.md](INSTALL.md)** - Installation and troubleshooting guide
- **[CHANGELOG.md](CHANGELOG.md)** - Version history and changes

## 🎯 Sort Modes

- `ITEM_TYPE` - Sort by category (weapons, tools, food, etc.)
- `RARITY` - Sort by Minecraft rarity (common → legendary)
- `NAME_AZ` - Alphabetical sorting
- `QUANTITY` - Sort by stack size (largest first)
- `DURABILITY` - Sort by remaining durability

## 🔧 Building from Source

### Prerequisites
- Java 21 JDK
- Git

### Build Steps
```bash
git clone https://github.com/devxkamlesh/autoinventorysorter.git
cd autoinventorysorter
./gradlew build
```

The compiled JAR will be in `build/libs/`

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

### Development Setup
1. Clone the repository
2. Import into your IDE (IntelliJ IDEA recommended)
3. Run `./gradlew genSources` to generate Minecraft sources
4. Make your changes
5. Test thoroughly
6. Submit a PR

## 🐛 Bug Reports

Found a bug? Please [open an issue](https://github.com/devxkamlesh/autoinventorysorter/issues) with:
- Minecraft version
- Fabric Loader version
- Fabric API version
- Mod version
- Steps to reproduce
- Crash log (if applicable)

## 📊 Performance

- **Memory Overhead**: ~2MB
- **CPU Usage**: <0.1% average
- **Tick Impact**: Negligible
- **Sort Operation**: ~5ms for full inventory

## 🎨 Screenshots

*Coming soon!*

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Fabric](https://fabricmc.net/) - Mod loader and API
- [Minecraft](https://www.minecraft.net/) - The game
- Community feedback and feature requests

## 📞 Contact

- **GitHub**: [@devxkamlesh](https://github.com/devxkamlesh)
- **Website**: [devxkamlesh.vercel.app](https://devxkamlesh.vercel.app)
- **Issues**: [GitHub Issues](https://github.com/devxkamlesh/autoinventorysorter/issues)

## ⭐ Support

If you like this mod, please consider:
- ⭐ Starring the repository
- 🐛 Reporting bugs
- 💡 Suggesting features
- 🔀 Contributing code

---

**Made with ❤️ for the Minecraft community**
