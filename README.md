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



## Pembagian tugas WBD IF3110
### REST
1. Menambah transaksi baru : 13517096
2. Mengubah status transaksi : 13517108
3. Mengembalikan seluruh data transaksi : 13517021
4. Skema database : 13517108

### SOAP
1. Validasi no rekening : 13517021
2. Memberi data rekening nasabah : 13517096
3. Transaksi transfer : 13517108
4. Membuat virtual account : 13517021
5. Mengecek data transaksi : 13517096

### ReactJS
1. Template bank-pro : 13517021
2. Login : 13517021
3. Transfer : 13517096
4. Riwayat : 13517108

### Perubahan Engima
1. Pemgambilan data film ke home : 13517108
2. Detail film dan search : 13517108
3. Buy ticket : 13517021
4. Transaction history : 13517096
5. Transaksi tiket film : 13517021


### Untuk tugas DPPL IF3159 Bagian WS-Bank
1. Continous Integration : Linting dan Testing 13517021
2. Eksplorasi EC2 : 13517021
3. Deployment BankPro ke EC2: 13517021
4. Continuous Deployment Pipeline: 13517021
5. Deployment Database ke Amazon RDS: 13517096


__Service WS-Bank dapat diakses melalui link berikut:__
~~[http://ec2-52-90-76-43.compute-1.amazonaws.com:8080/ws-bank-1.0/services/bankpro](http://ec2-52-90-76-43.compute-1.amazonaws.com:8080/ws-bank-1.0/services/bankpro)~~
Link URL ganti karena instancenya ke restart <br/>
Link deployment yang baru: [http://ec2-34-226-143-116.compute-1.amazonaws.com:8080/ws-bank-1.0/services/bankpro](http://ec2-34-226-143-116.compute-1.amazonaws.com:8080/ws-bank-1.0/services/bankpro)
