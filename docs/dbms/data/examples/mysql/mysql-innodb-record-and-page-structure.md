# MySQL InnoDB Record and Page Structure

|时间|内容|
|:---|:---|
|20210510|kick off: add Record structure.|

## Record

- references: https://dev.mysql.com/doc/internals/en/innodb-record-structure.html
- innodb_digrams: https://github.com/jeremycole/innodb_diagrams/blob/master/images/InnoDB_Structures/Record%20Format%20-%20Overview.png

物理记录的三个组成部分:

```
+---------------------+
| FIELD START OFFSETS |   F or 2F bytes, F: number of fields
+---------------------+
| EXTRA BYTES         |   6 bytes
+---------------------+
| FIELD CONTENTS      |   dependent on content
+---------------------+
```

- FIELD START OFFSETS: 一组数字, 数字体现字段从何处开始; ==not correct==
- EXTRA BYTES: 固定长度的头部;
- FIELD CONTENTS: 实际数据.

记录的Origin或Zero Point是指FIELD CONTENTS的第一个字节. 称有一个指向记录的指针, 这个指针指向Origin.

### FIELD START OFFSETS

一个数字列表, 每个数字表示相对于Origin下一个字段开始的位置(偏移量offset). 列表中项是逆序的, 即第一个字段的偏移量在列表的尾部.

偏移量为一个字节:

```
 0  1  2  3  4  5  6  7
+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+

Bit    0: NULL
Bits 1-7: the actual offset, [0, 127]
```

偏移量为两个字节:


```
 0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

Bit     0: NULL
Bit     1: 0 if field is on same page as offset;
           1 if field and offset are on different pages
Bits 2-15: the actual offset, [0, 16383]
```

### EXTRA BYTES

==not correct==

|Name|Size(bit)|Description|
|:---|:---|:---|
|()|1|unused or unknown|
|()|1|unused or unknown|
|deleted_flag|1|1 if record is deleted|
|min_rec_flag|1|1 if record is predefined minimum record|
|n_owned|4|number of records owned by this record|
|heap_no|13|record's order number in heap of index page|
|**n_fields**|10|number of fields in this record, 1 to 1023|
|**1byte_offs_flag**|1|1 if each FIELD START OFFSETS is 1 byte long<br/>0 if 2 bytes long|
|next 16 bits|16|pointer to next record in page|

### FIELD CONTENTS

Field按定义的顺序存储, field之间没有标记, record的尾部没有标记或填充.

> refman-5.7-en 14.3 InnoDB Multi-Versioning
>
> Internally, InnoDB adds three fields to each row stored in the database.
>> A 6-byte **DB_TRX_ID** field indicates the transaction identifier for the last transaction that inserted or updated the row. Also, a deletion is treated internally as an update where a special bit in the row is set to mark it as deleted.
>
>> Each row also contains a 7-byte **DB_ROLL_PTR** field called the roll pointer. The roll pointer points to an undo log record written to the rollback segment. If the row was updated, the undo log record contains the information necessary to rebuild the content of the row before it was updated.
>
>> A 6-byte **DB_ROW_ID** field contains a row ID that increases monotonically as new rows are inserted. If InnoDB generates a clustered index automatically, the index contains row ID values. Otherwise, the DB_ROW_ID column does not appear in any index.

### Example

``` sql
-- version: 5.7.21-log
CREATE TABLE `t` (
  `f1` varchar(3) DEFAULT NULL,
  `f2` varchar(3) DEFAULT NULL,
  `f3` varchar(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO T VALUES ('PP', 'PP', 'PP');
INSERT INTO T VALUES ('Q', 'Q', 'Q');
INSERT INTO T VALUES ('R', NULL, NULL);
```

```
00bff0  00 00 00 00 00 00 00 00 bb 47 30 97 1f 2c 5a 7f  .........G0..,Z.
00c000  d3 58 5e b3 00 00 00 03 ff ff ff ff ff ff ff ff  .X^.............
00c010  00 00 00 01 1f 2c 65 9e 45 bf 00 00 00 00 00 00  .....,e.E.......
00c020  00 00 00 00 00 58 00 02 00 d4 80 05 00 00 00 00  .....X..........
00c030  00 c0 00 02 00 02 00 03 00 00 00 00 00 00 00 00  ................
00c040  00 00 00 00 00 00 00 00 00 72 00 00 00 58 00 00  .........r...X..
00c050  00 02 00 f2 00 00 00 58 00 00 00 02 00 32 01 00  .......X.....2..
00c060  02 00 1e 69 6e 66 69 6d 75 6d 00 04 00 0b 00 00  ...infimum......
00c070  73 75 70 72 65 6d 75 6d 02 02 02 00 00 00 10 00  supremum........
00c080  22 00 00 00 00 03 00 00 00 00 00 37 14 b1 00 00  "..........7....
00c090  04 71 01 10 50 50 50 50 50 50 01 01 01 00 00 00  .q..PPPPPP......
00c0a0  18 00 1d 00 00 00 00 03 01 00 00 00 00 37 15 b2  .............7..
00c0b0  00 00 04 74 01 10 51 51 51 01 06 00 00 20 ff b0  ...t..QQQ.... ..
00c0c0  00 00 00 00 03 02 00 00 00 00 37 18 b4 00 00 04  ..........7.....
00c0d0  72 01 10 52 00 00 00 00 00 00 00 00 00 00 00 00  r..R............

02 02 02                      # Varaible field length (1-2 bytes per var. field)
00                            # nullable field bitmap
00 00 10 00 22                # EXTRA BYTES
00000000 00000000 00000000 00010000 00000000 00100010
00 00 00 00 03 00             # System Column: DB_ROW_ID
00 00 00 00 37 14             # System Column: DB_TRX_ID
B1 00 00 04 71 01 10          # System Column: DB_ROLL_PTR
50 50 50 50 50 50             # C094: + 0022 = C0B6

relative offset of next record: 00 22
status                        : 000
heap number                   : 00000000 00010 = 2
n_owned                       : 0
info                          : 0


01 01 01                      # Varaible field length (1-2 bytes per var. field)
00                            # nullable field bitmap
00 00 18 00 1D                # EXTRA BYTES
00000000 00000000 00000000 00011000 00000000 00011101
00 00 00 00 03 01             # System Column: DB_ROW_ID
00 00 00 00 37 15             # System Column: DB_TRX_ID
B2 00 00 04 74 01 10          # System Column: DB_ROLL_PTR
51 51 51                      # C0B6: + 001D = C0D3

relative offset of next record: 00 1D
status                        : 000
heap number                   : 00000000 00011 = 3
n_owned                       : 0
info                          : 0


01                            # Varaible field length (1-2 bytes per var. field)
06                            # nullable field bitmap: 0000 0110
00 00 20 FF B0                # EXTRA BYTES
00000110 00000000 00000000 00100000 11111111 10110000
00 00 00 00 03 02             # System Column: DB_ROW_ID
00 00 00 00 37 18             # System Column: DB_TRX_ID
B4 00 00 04 72 01 10          # System Column: DB_ROLL_PTR
52                            # C0D3: + FFB0 = 1 C083 - non-sense

relative offset of next record: FF B0
status                        : 000
heap number                   : 00000000 00100 = 4
n_owned                       : 0
info                          : 0
```

### Source Codes

- rem0rec.ic, rem0rec.h, rem0rec.c

``` c++
// innobase/include/rem0rec.ic

/* Offsets of the bit-fields in an old-style record. NOTE! In the table the
most significant bytes and bits are written below less significant.

        (1) byte offset		(2) bit usage within byte
        downward from
        origin ->	1	8 bits pointer to next record
                  2	8 bits pointer to next record
                  3	1 bit short flag
                    7 bits number of fields
                  4	3 bits number of fields
                    5 bits heap number
                  5	8 bits heap number
                  6	4 bits n_owned
                    4 bits info bits
*/

/* Offsets of the bit-fields in a new-style record. NOTE! In the table the
most significant bytes and bits are written below less significant.

        (1) byte offset		(2) bit usage within byte
        downward from
        origin ->	1	8 bits relative offset of next record
                  2	8 bits relative offset of next record
                                  the relative offset is an unsigned 16-bit
                                  integer:
                                  (offset_of_next_record
                                   - offset_of_this_record) mod 64Ki,
                                  where mod is the modulo as a non-negative
                                  number;
                                  we can calculate the offset of the next
                                  record with the formula:
                                  relative_offset + offset_of_this_record
                                  mod UNIV_PAGE_SIZE
                  3	3 bits status:
                                        000=conventional record
                                        001=node pointer record (inside B-tree)
                                        010=infimum record
                                        011=supremum record
                                        1xx=reserved
                    5 bits heap number
                  4	8 bits heap number
                  5	4 bits n_owned
                    4 bits info bits
*/
```

``` c++
// innobase/rem/rem0rec.cc

/*			PHYSICAL RECORD (NEW STYLE)
                        ===========================
The physical record, which is the data type of all the records
found in index pages of the database, has the following format
(lower addresses and more significant bits inside a byte are below
represented on a higher text line):
| length of the last non-null variable-length field of data:
  if the maximum length is 255, one byte; otherwise,
  0xxxxxxx (one byte, length=0..127), or 1exxxxxxxxxxxxxx (two bytes,
  length=128..16383, extern storage flag) |
...
| length of first variable-length field of data |
| SQL-null flags (1 bit per nullable field), padded to full bytes |
| 1 or 2 bytes to indicate number of fields in the record if the table
  where the record resides has undergone an instant ADD COLUMN
  before this record gets inserted; If no instant ADD COLUMN ever
  happened, here should be no byte; So parsing this optional number
  requires the index or table information |
| 4 bits used to delete mark a record, and mark a predefined
  minimum record in alphabetical order |
| 4 bits giving the number of records owned by this record
  (this term is explained in page0page.h) |
| 13 bits giving the order number of this record in the
  heap of the index page |
| 3 bits record type: 000=conventional, 001=node pointer (inside B-tree),
  010=infimum, 011=supremum, 1xx=reserved |
| two bytes giving a relative pointer to the next record in the page |
ORIGIN of the record
| first field of data |
...
| last field of data |
```

## Page

- references: https://dev.mysql.com/doc/internals/en/innodb-page-structure.html
