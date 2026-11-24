# Sanchi's Module: File Handling & Reports

**Owner**: Sanchi  
**Module**: File Handling & Reports (Evaluation 1)  
**Package**: `package nexcruit;`  

---

## Source Files in Repository:
* Implementation:
  - [`src/nexcruit/FileManager.java`](../src/nexcruit/FileManager.java): Java object serialization and deserialization (`users.dat`, `jobs.dat`, `applications.dat`).
  - [`src/nexcruit/ReportManager.java`](../src/nexcruit/ReportManager.java): Dynamic recruitment statistics and job-wise reporting.
* Tests:
  - [`src/nexcruit/FileReportTest.java`](../src/nexcruit/FileReportTest.java): Complete 12-test verification suite.
* Detailed Documentation:
  - [`docs/SANCHI_FILE_REPORT_MODULE.md`](../docs/SANCHI_FILE_REPORT_MODULE.md): Comprehensive module architecture, formulas, and viva guide.

---

## Quick Compilation & Execution:
```powershell
$env:PATH = "C:\Program Files\Java\jdk-26.0.1\bin;$env:PATH"
$javaFiles = Get-ChildItem -Recurse -Path src, shanaya -Filter *.java | Select-Object -ExpandProperty FullName
javac -d bin -cp "src;shanaya" $javaFiles
java -cp bin nexcruit.FileReportTest
```
