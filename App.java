package com.mylauncher;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class App {
    // 🔹 Укажи свой GitHub-репозиторий с программой
    private static final String REPO_URL = "https://github.com/uniklue/my-launcher.git";
    private static final String LOCAL_DIR = "C:\\my-launcher\\launcher"; // Папка с программой

    public static void main(String[] args) {
        try {
            File localRepo = new File(LOCAL_DIR);
            
            // Если папки нет, значит, приложение запускается впервые → клонируем репозиторий
            if (!localRepo.exists()) {
                System.out.println("Клонируем репозиторий...");
                Git.cloneRepository()
                        .setURI(REPO_URL)
                        .setDirectory(localRepo)
                        .call();
            } else {
                System.out.println("Проверяем обновления...");
                Git git = Git.open(localRepo);
                git.pull().call(); // Скачиваем обновления
            }

            // Запускаем обновлённое приложение
            restartApplication();
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void restartApplication() throws IOException {
        String javaBin = System.getProperty("java.home") + "\\bin\\java";
        File jarFile = new File(LOCAL_DIR, "app.jar"); // Файл с программой

        if (!jarFile.exists()) {
            System.err.println("Файл app.jar не найден!");
            return;
        }

        // Запускаем обновлённое приложение
        ProcessBuilder builder = new ProcessBuilder(javaBin, "-jar", jarFile.getAbsolutePath());
        builder.start();
        System.exit(0);  // Завершаем текущий процесс
    }
}
