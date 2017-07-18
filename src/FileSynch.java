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

        System.out.println("Скопировано файлов: " + copyFilesInFolder(sourcePath, destPath));

    }

    private static int copyFilesInFolder(String sourcePath, String destPath) {
        int result = 0;
        File sourceDir = new File(sourcePath);
        if (!sourceDir.isDirectory()) {
            System.out.println("Каталог-источник " + sourcePath + " не существует или не является каталогом");
            return result;
        }
        File destDir = new File(destPath);
        if (!destDir.isDirectory()) {
            System.out.println("Создаем каталог " + destDir.getPath());
            if (!destDir.mkdir()) {
                System.out.println("Невозможно создать каталог-приемник " + destPath + ".");
                return result;
            }
        }

        File[] fList = sourceDir.listFiles();
        for (File file : fList) {
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
            } else if (file.isDirectory()){
                result+=copyFilesInFolder(file.getPath(), newDestPath);
            }
        }
        return result;
    }
}
