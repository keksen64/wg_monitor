import java.io.File;
import java.util.Arrays;

public class FileCleaner {

    public static void cleanOldFiles(String directoryPath, String filePrefix, int maxFiles) {
        // Получаем список файлов в директории
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Указанная директория не существует или не является директорией: " + directoryPath);
            return;
        }

        // Фильтруем файлы, которые начинаются с заданного префикса
        File[] files = directory.listFiles((dir, name) -> name.startsWith(filePrefix));
        if (files == null || files.length <= maxFiles) {
            return; // Если файлов меньше или равно maxFiles, ничего не делаем
        }

        // Сортируем файлы по времени последнего изменения (от самого старого к самому новому)
        Arrays.sort(files, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

        // Удаляем самые старые файлы, пока их количество больше maxFiles
        int filesToDelete = files.length - maxFiles;
        for (int i = 0; i < filesToDelete; i++) {
            File fileToDelete = files[i];
            if (fileToDelete.delete()) {
                System.out.println("Удалён файл: " + fileToDelete.getName());
            } else {
                System.err.println("Не удалось удалить файл: " + fileToDelete.getName());
            }
        }
    }
}