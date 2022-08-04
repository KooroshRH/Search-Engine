import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileWorker
{
    private Indexer indexer;
    private int docId;

    public FileWorker(Indexer indexer)
    {
        this.indexer = indexer;
        this.docId = 1;
    }

    public void fileIndex(String filePath) throws IOException
    {
        FileInputStream file = new FileInputStream(new File(filePath));
        XSSFWorkbook excelFile = new XSSFWorkbook(file);
        boolean isFirstRow = true;
        for (Row row : excelFile.getSheetAt(0)) {
            if (isFirstRow)
            {
                isFirstRow = false;
                continue;
            }
            indexer.addToIndex(
                    String.valueOf(row.getCell(1)),
                    docId++,
                    String.valueOf(row.getCell(2)),
                    "all");
        }
        excelFile.close();
        file.close();
    }

    public void labeledFileIndex(String filePath) throws IOException
    {
        FileInputStream file = new FileInputStream(new File(filePath));
        XSSFWorkbook excelFile = new XSSFWorkbook(file);
        boolean isFirstRow = true;
        for (Row row : excelFile.getSheetAt(0)) {
            if (isFirstRow)
            {
                isFirstRow = false;
                continue;
            }
            indexer.addToIndex(
                    String.valueOf(row.getCell(1)),
                    (int) row.getCell(0).getNumericCellValue(),
                    String.valueOf(row.getCell(3)),
                    String.valueOf(row.getCell(2))
                    );
        }
        excelFile.close();
        file.close();
    }
}
