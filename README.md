# 🎮 RhythmGame - Java 节奏音乐游戏

一个基于 Java Swing 的下落节奏音乐游戏。
此为基础的GUI，没有任何花里胡哨的东西或者精美的GUI。


![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)

## ✨ 功能特性

- 🎵 **音乐播放** - 支持 WAV, MP3 等多种音频格式
- 🎯 **精确判定** - 多级判定系统 (Perfect/Good/OK/Miss)
- 📊 **计分系统** - 实时分数和连击数显示
- 🎼 **多关卡** - 轻松添加和切换不同歌曲
- 📝 **JSON 谱面** - 灵活的谱面格式，易于编辑
- 🤖 **自动生成** - Python 脚本自动分析音乐生成谱面

## 🎮 游戏截图

```
游戏界面:
┌─────────────────────────────────┐
│  选择歌曲: [示例歌曲 ▼] [开始]  │
├─────────────────────────────────┤
│                                 │
│     ●       ●       ●       ●   │  ← 下落的音符
│     │       │       │       │   │
│     │       │       │       │   │
│     │       │       │       │   │
│  ───────────────────────────────│  ← 判定线
│     D       F       J       K   │  ← 按键提示
│                                 │
│  分数: 12500    连击: 25         │
└─────────────────────────────────┘
```

## 🚀 快速开始

### 系统要求

- Java 8 或更高版本
- 支持 Windows, macOS, Linux

### 安装步骤

1. **克隆仓库**
```bash
git clone https://github.com/MEMZ-JJY/RhythmGame.git
cd RhythmGame
```

2. **下载依赖库**

下载 JSON 库并放入 `lib` 目录：
```bash
mkdir -p lib
cd lib
# Windows (PowerShell)
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar" -OutFile "json-20231013.jar"


```

3. **编译项目**


```bash
powershell

.\co2mpile.ps1
```



4. **运行游戏**


```bash
powershell

.\run.ps1
```
如果run.ps1文件没有出现，请手动创建
```bash
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "--- Starting RhythmGame ---" -ForegroundColor Cyan
Write-Host "By Sakurazuki | https://moongs.asia/" -ForegroundColor Cyan

if (-not (Test-Path "bin")) {
    Write-Host "[ERROR] 'bin' folder not found. Please run compile.ps1 first." -ForegroundColor Red
    Pause
    exit
}


java -cp "bin;lib/*" com.rhythmgame.Main


if ($LASTEXITCODE -ne 0) {
    Write-Host "`n[INFO] Game exited with code: $LASTEXITCODE" -ForegroundColor Yellow
    Pause
}
```

## 🎵 添加歌曲

### 方式1: 手动创建谱面

1. 在 `songs/` 目录下创建歌曲文件夹：
```bash
mkdir songs/my-awesome-song
```

2. 将音乐文件放入文件夹并命名为 `music.wav`

3. 创建 `chart.json` 谱面文件：
```json
{
  "song_name": "我的歌曲",
  "artist": "艺术家",
  "bpm": 120,
  "difficulty": "Normal",
  "music_path": "songs/my-awesome-song/music.wav",
  "notes": [
    {"time": 1000, "lane": 0},
    {"time": 1500, "lane": 1}
  ]
}
```

详细的谱面制作指南请查看：[docs/chart-format-guide.md](docs/chart-format-guide.md)

### 方式2: 自动生成谱面

使用 Python 脚本自动分析音乐并生成谱面：

1. **安装 Python 依赖**
```bash
pip install librosa numpy
```

2. **运行生成脚本**
```bash
python tools/auto_chart_generator.py songs/my-song/music.wav normal
```

参数说明：
- 第一个参数：音乐文件路径
- 第二个参数：难度 (easy/normal/hard)，默认为 normal

3. **微调谱面**

自动生成的谱面可能需要手动调整，建议使用音乐编辑软件（如 Audacity）查看音乐节拍，然后修改 `chart.json` 中的音符时间。

## 🎮 游戏玩法

### 按键说明

- **D 键** - 左侧第1轨道
- **F 键** - 左侧第2轨道
- **J 键** - 右侧第3轨道
- **K 键** - 右侧第4轨道

### 判定系统

| 判定 | 时间差 | 分数 |
|------|--------|------|
| Perfect | < 50ms | 100 + 连击加成 |
| Good | 50-100ms | 50 + 连击加成 |
| OK | 100-150ms | 20 + 连击加成 |
| Miss | > 150ms | 0 (连击清零) |

## 📁 项目结构

```
RhythmGame/
├── src/                      # Java 源代码
│   └── com/rhythmgame/
│       ├── Main.java         # 程序入口
│       ├── GameWindow.java   # 游戏窗口
│       ├── GamePanel.java    # 游戏面板
│       ├── Note.java         # 音符类
│       ├── Chart.java        # 谱面类
│       ├── ChartLoader.java  # 谱面加载器
│       ├── MusicPlayer.java  # 音乐播放器
│       └── ScoreManager.java # 分数管理器
├── songs/                    # 歌曲文件夹
│   └── example-song/
│       ├── chart.json        # 谱面文件
│       └── music.wav         # 音乐文件
├── docs/                     # 文档
│   ├── chart-format-guide.md # 谱面格式指南
│   └── git-tutorial.md       # Git 使用教程
├── tools/                    # 工具脚本
│   └── auto_chart_generator.py # 自动谱面生成器
├── lib/                      # 外部库
│   └── json-20231013.jar     # JSON 解析库
├── compile.bat               # Windows 编译脚本
├── compile.sh                # Linux/Mac 编译脚本
├── run.bat                   # Windows 运行脚本
├── run.sh                    # Linux/Mac 运行脚本
├── .gitignore                # Git 忽略文件
└── README.md                 # 项目说明
```



## 📝 待开发功能

- [ ] 长按音符支持
- [ ] 更多音效反馈
- [ ] 难度选择界面
- [ ] 成绩保存和排行榜
- [ ] 更多视觉效果
- [ ] 自定义按键设置
- [ ] 谱面编辑器 GUI

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情



## 📧 联系方式

如有问题或建议，欢迎通过以下方式联系：

- 提交 Issue
- Email: admin@moongs.asia
- 网站：https://moongs.asia

---

