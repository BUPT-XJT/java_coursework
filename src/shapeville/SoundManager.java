package shapeville;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {
    
    public void playSound(String soundType) {
        new Thread(() -> {
            try {
                // 从资源文件夹加载音频
                URL soundUrl = getClass().getResource("/sounds/" + soundType + ".wav");
                if (soundUrl == null) {
                    System.out.println("找不到音效文件: " + soundType);
                    return;
                }

                // 获取音频流
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundUrl);
                
                // 获取音频格式
                AudioFormat format = audioIn.getFormat();
                
                // 打开音频设备
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip clip = (Clip) AudioSystem.getLine(info);
                
                // 播放设置
                clip.open(audioIn);
                clip.start();
                Thread.sleep(3000);

                // 自动关闭资源（播放完成后）
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        try { audioIn.close(); } catch (Exception e) {}
                    }
                });

            } catch (Exception e) {
                System.out.println("播放音效失败: " + e.getMessage());
            }
        }).start();
    }

    public static void main(String[]args){
        SoundManager soundManager = new SoundManager();
        System.out.println("播放正确音效");
        soundManager.playSound("correct");
    }
}
