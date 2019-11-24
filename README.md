# WS - Bank

## Deskripsi Web Service
Web Service Bank diimplementasikan di atas java servlet menggunakan JAX-WS dengan protokol SOAP. Implementasi dilakukan di IDE Intellij IDEA Ultimate dengan server Glassfish 5.
Web Service Bank memiliki layanan :
1. Validasi nomor rekening.
2. Memberikan data rekening seorang nasabah.
3. Melakukan transfer dengan input nomor rekening pengirim, nomor rekening/ akun virtual penerima, dan jumlah uang yang ditransfer.
4. Membuat akun virtual untuk suatu nomor rekening.
5. mengecek ada atau tidak sebuah transaksi dalam suatu rentang waktu. 


## Basis data yang digunakan
Web service menggunakan baisdata ws_bank dengan tiga tabel yaitu tabel nasabah, transaksi dan virtual_account.

### Tabel 1 : nasabah
##### id | name | account_num | balance

### Tabel 2 : transaksi
##### id | sender | receiver | date | amount 

### Tabel 3 : nasabah
##### id | virtual_account_num | account_num



