import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    static void main() {
        Path enteredDir = inputDir();
        Path[] workDirs = createDirIfNotExists(enteredDir);
        Path[] listFiles = listDir(enteredDir);
        sortFiles(listFiles, workDirs);
    }
    public static Path inputDir() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter absolute path to directory: ");
        return Path.of(scanner.nextLine());
    }
    public static Path[] createDirIfNotExists(Path enteredDir) {
        Path[] reqDirs = {
                Path.of("Images"),
                Path.of("Videos"),
                Path.of("Documents"),
                Path.of("Musics"),
                Path.of("Archives"),
                Path.of("Other")};
        Path[] workDirs = new Path[reqDirs.length];
        for (byte i = 0; i < reqDirs.length; i++) {
            workDirs[i] = enteredDir.resolve(reqDirs[i]);
            try {
                Files.createDirectories(workDirs[i]);
            } catch (IOException e) {
                System.out.print("\nErr: " + e.getMessage());
            }
        }
        return workDirs;
    }
    public static Path[] listDir(Path enteredDir) {
        Path[] listFiles = null;
        try (Stream<Path> files = Files.list(enteredDir)) {
            listFiles = files.toArray(Path[]::new);
        } catch (IOException e) {
            System.out.print("\nErr: " + e.getMessage());
        }
        return listFiles;
    }
    public static void sortFiles(Path[] listFiles, Path[] workDirs) {
        for (Path p : listFiles) {
            if (Files.isDirectory(p))
                continue;
            String currFile = p.getFileName().toString();
            int extIndex = currFile.lastIndexOf(".");
            String ext = currFile.substring(extIndex + 1).toLowerCase();
            try {
                switch (ext) {
                    case "png", "jpg", "jpeg", "webp", "bmp" -> Files.move(p, workDirs[0].resolve(currFile));
                    case "mp4", "mkv", "mov", "avi" -> Files.move(p, workDirs[1].resolve(currFile));
                    case "docx", "pdf", "txt", "rtf", "csv", "pptx" -> Files.move(p, workDirs[2].resolve(currFile));
                    case "mp3", "flac", "aiff", "ogg" -> Files.move(p, workDirs[3].resolve(currFile));
                    case "zip", "7z", "rar", "tar", "gz" -> Files.move(p, workDirs[4].resolve(currFile));
                    default -> Files.move(p, workDirs[5].resolve(currFile));
                }
            } catch (IOException e) {
                System.out.println("Err: " + e.getMessage());
            }
        }
    }
}