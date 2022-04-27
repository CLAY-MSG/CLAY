package xyz.sgmi.clay.csv;

import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvRowHandler;
import lombok.Data;

/**
 * 统计当前文件有多少行
 * @Author: MSG
 * @Date:
 * @Version 1.0
 */
@Data
public class CountFileRowHandler implements CsvRowHandler {

    private long rowSize;

    @Override
    public void handle(CsvRow row) {
        rowSize++;
    }

    public long getRowSize() {
        return rowSize;
    }
}

