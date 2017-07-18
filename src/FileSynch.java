import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class FileSynch {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Недостаточно параметров. Формат вызова: java FileSynch каталог_источник каталог_приемник");
            return;
        }
        String sourcePath = args[0];
        String destPath = args[1];

        startSynch(sourcePath, destPath);

    }

    private static void startSynch(String sourcePath, String destPath){
        File sourceDir = new File(sourcePath);
        if (!sourceDir.isDirectory()) {
            System.out.println("Каталог-источник " + sourcePath + " не существует или не является каталогом");
            return;
        }
        File destDir = new File(destPath);
        if (destDir.exists()) {
            System.out.println("Удалено файлов и директорий:" + removeFilesFromDir(sourcePath, destPath));
        }

        System.out.println("Скопировано файлов: " + copyFilesInFolder(sourcePath, destPath));

    }

    private static int copyFilesInFolder(String sourcePath, String destPath) {
        int result = 0;
        File sourceDir = new File(sourcePath);
        File destDir = new File(destPath);
        if (!destDir.isDirectory()) {
            System.out.println("Создаем каталог " + destDir.getPath());
            if (!destDir.mkdir()) {
                System.out.println("Невозможно создать каталог-приемник " + destPath + ".");
                return 0;
            }
        }
        File[] sourceFiles = sourceDir.listFiles();
        for (File file : sourceFiles) {
            String newDestPath = destPath + file.getPath().substring(sourcePath.length());
            if (file.isFile()) {
                File newFile = new File(newDestPath);
                if (!newFile.exists() || file.length() != newFile.length())
                    try {
                        System.out.println("Копируем файл из " + file.getPath() + " в " + newFile.getPath());
                        Files.copy(file.toPath(), newFile.toPath(), REPLACE_EXISTING);
                        result++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            } else if (file.isDirectory()) {
                result += copyFilesInFolder(file.getPath(), newDestPath);
            }
        }

        return result;
    }

    private static int removeFilesFromDir(String sourcePath, String destPath) {
        int result = 0;
        File destDir = new File(destPath);
        File[] destFiles = destDir.listFiles();
        for (File file : destFiles) {
            String checkFilePath = sourcePath + file.getPath().substring(destPath.length());
            File checkFile = new File(checkFilePath);
            if (file.isFile() && !checkFile.exists()) {
                try {
                    System.out.println("Удаляем файл " + file.getPath());
                    Files.delete(file.toPath());
                    result++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (file.isDirectory()  && !checkFile.exists()){
                result+=removeFilesFromDir(checkFilePath, file.getPath());
                try {
                    System.out.println("Удаляем директорию " + file.getPath());
                    Files.delete(file.toPath());
                    result++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
