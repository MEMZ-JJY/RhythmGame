#!/usr/bin/env python3
"""
音乐谱面自动生成器
根据音频文件自动生成节奏游戏谱面
"""

import json
import sys
import os
import numpy as np

try:
    import librosa
    import librosa.display
except ImportError:
    print("错误: 需要安装 librosa 库")
    print("请运行: pip install librosa")
    sys.exit(1)


class ChartGenerator:
    def __init__(self, audio_path):
        self.audio_path = audio_path
        self.y = None
        self.sr = None
        self.bpm = None
        self.beats = None
        
    def load_audio(self):
        """加载音频文件"""
        print(f"正在加载音频文件: {self.audio_path}")
        self.y, self.sr = librosa.load(self.audio_path)
        print(f"采样率: {self.sr} Hz")
        print(f"音频长度: {len(self.y) / self.sr:.2f} 秒")
        
    def detect_bpm(self):
        """检测BPM"""
        print("正在检测 BPM...")
        tempo, _ = librosa.beat.beat_track(y=self.y, sr=self.sr)
        self.bpm = int(tempo)
        print(f"检测到 BPM: {self.bpm}")
        
    def detect_beats(self):
        """检测节拍位置"""
        print("正在检测节拍...")
        _, beat_frames = librosa.beat.beat_track(y=self.y, sr=self.sr)
        self.beats = librosa.frames_to_time(beat_frames, sr=self.sr)
        print(f"检测到 {len(self.beats)} 个节拍")
        
    def detect_onsets(self):
        """检测音符起始点（更精确）"""
        print("正在检测音符起始点...")
        onset_frames = librosa.onset.onset_detect(y=self.y, sr=self.sr)
        onsets = librosa.frames_to_time(onset_frames, sr=self.sr)
        print(f"检测到 {len(onsets)} 个音符起始点")
        return onsets
        
    def generate_notes(self, difficulty='normal'):
        """生成谱面音符"""
        print(f"正在生成 {difficulty} 难度谱面...")
        
        # 使用音符起始点而不是节拍
        onsets = self.detect_onsets()
        notes = []
        
        # 根据难度调整密度
        if difficulty == 'easy':
            step = 4  # 每4个音符取1个
        elif difficulty == 'normal':
            step = 2  # 每2个音符取1个
        elif difficulty == 'hard':
            step = 1  # 使用所有音符
        else:
            step = 2
            
        # 生成音符
        for i, onset_time in enumerate(onsets[::step]):
            time_ms = int(onset_time * 1000)  # 转换为毫秒
            
            # 根据时间和音高分配轨道
            lane = i % 4  # 简单循环分配
            
            # 可选：根据频谱能量分配轨道
            # 这里可以添加更复杂的逻辑
            
            notes.append({
                "time": time_ms,
                "lane": lane
            })
            
        print(f"生成了 {len(notes)} 个音符")
        return notes
        
    def create_chart_json(self, output_path, difficulty='normal'):
        """创建完整的谱面JSON"""
        
        # 从文件名提取歌曲信息
        song_folder = os.path.dirname(output_path)
        song_name = os.path.basename(song_folder)
        
        # 生成音符
        notes = self.generate_notes(difficulty)
        
        # 创建谱面数据
        chart_data = {
            "song_name": song_name,
            "artist": "Unknown Artist",
            "bpm": self.bpm,
            "difficulty": difficulty.capitalize(),
            "music_path": os.path.join(song_folder, os.path.basename(self.audio_path)),
            "notes": notes
        }
        
        # 保存JSON文件
        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump(chart_data, f, indent=2, ensure_ascii=False)
            
        print(f"\n谱面已生成: {output_path}")
        print(f"歌曲名称: {chart_data['song_name']}")
        print(f"BPM: {chart_data['bpm']}")
        print(f"难度: {chart_data['difficulty']}")
        print(f"音符数量: {len(notes)}")


def main():
    print("=" * 60)
    print("音乐谱面自动生成器")
    print("=" * 60)
    
    # 检查命令行参数
    if len(sys.argv) < 2:
        print("\n用法: python auto_chart_generator.py <音频文件路径> [难度]")
        print("\n难度选项: easy, normal, hard")
        print("默认: normal")
        print("\n示例:")
        print("  python auto_chart_generator.py songs/my-song/music.wav")
        print("  python auto_chart_generator.py songs/my-song/music.mp3 hard")
        sys.exit(1)
        
    audio_path = sys.argv[1]
    difficulty = sys.argv[2] if len(sys.argv) > 2 else 'normal'
    
    # 验证文件存在
    if not os.path.exists(audio_path):
        print(f"错误: 文件不存在: {audio_path}")
        sys.exit(1)
        
    # 验证难度参数
    if difficulty not in ['easy', 'normal', 'hard']:
        print(f"警告: 未知难度 '{difficulty}'，使用 'normal'")
        difficulty = 'normal'
        
    # 生成输出路径
    audio_dir = os.path.dirname(audio_path)
    output_path = os.path.join(audio_dir, 'chart.json')
    
    try:
        # 创建生成器并处理
        generator = ChartGenerator(audio_path)
        generator.load_audio()
        generator.detect_bpm()
        generator.detect_beats()
        generator.create_chart_json(output_path, difficulty)
        
        print("\n✓ 谱面生成成功!")
        print(f"\n提示: 请根据实际音乐效果手动调整 {output_path}")
        print("建议使用音乐编辑软件（如Audacity）进行精细调整")
        
    except Exception as e:
        print(f"\n✗ 生成失败: {str(e)}")
        import traceback
        traceback.print_exc()
        sys.exit(1)


if __name__ == "__main__":
    main()
