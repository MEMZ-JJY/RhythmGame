示例歌曲文件夹
================

本文件夹包含示例谱面 (chart.json)，但不包含实际的音乐文件。

如何添加音乐：
-------------

1. 准备一个音频文件（.wav 或 .mp3 格式）

2. 将音频文件重命名为 music.wav 或 music.mp3

3. 将其放入本文件夹

4. 如果需要，修改 chart.json 中的 music_path 字段

或者使用自己的歌曲：
-----------------

创建新的歌曲文件夹：

1. 在 songs/ 目录下创建新文件夹，如 "my-song"
2. 将音乐文件放入该文件夹
3. 创建或生成 chart.json 谱面文件

使用 Python 脚本自动生成谱面：

    python tools/auto_chart_generator.py songs/my-song/music.wav

详细说明请参考：docs/chart-format-guide.md
