package id.sch.smkn13bdg.adhi.brilinkadminarkan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.print.UnicodeFormatter;

public class PrintActivity extends Activity implements Runnable{

    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

    String nokartu, rektujuan, nominal, bank, tarif, jenistransaksi, penerima, kodebank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);


        //ambil data dari fragment
        jenistransaksi = getIntent().getStringExtra("jenis_transksi");
        nokartu = getIntent().getStringExtra("nokartu");
        rektujuan = getIntent().getStringExtra("rektujuan");
        nominal = getIntent().getStringExtra("nominal");
        bank = getIntent().getStringExtra("bank");
        tarif = getIntent().getStringExtra("tarif");
        penerima = getIntent().getStringExtra("penerima");
        kodebank = getIntent().getStringExtra("kode");


        int a = Integer.parseInt(nominal);
        int b;
        int total;
        if (tarif.equals("")){
            b = 0;
        }else{
            b = Integer.parseInt(tarif);
        }

        total = a + b;

        final String totalbayar = String.valueOf(total);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        final String waktu = mdformat.format(calendar.getTime());

        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat mdformat02 = new SimpleDateFormat("dd-MM-yyyy");
        final String tanggal = mdformat02.format(calendar1.getTime());

        mScan = (Button) findViewById(R.id.Scan);
        mScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(PrintActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,
                                REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(PrintActivity.this,
                                DeviceListActivity.class);
                        startActivityForResult(connectIntent,
                                REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });

        mPrint = (Button) findViewById(R.id.mPrint);
        mPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                Thread t = new Thread() {
                    public void run() {
                        try {
                            OutputStream os = mBluetoothSocket
                                    .getOutputStream();
                            String BILL = "";

                            BILL = "           BANK BRI    \n"
                                    + "             BRILink     \n " +
                                    "          ARKAN FOTO    \n" +
                                    " Jln.Cijerah 2 Blok 19 No. 147 \n" +
                                    "    No.Agen :000001370101851 \n";
                            BILL = BILL
                                    + "================================\n";


                            BILL = BILL + "BUKTI TRANSAKSI";
                            BILL = BILL + "\n";
                            BILL = BILL + "TANGGAL : " + tanggal + "\n";
                            BILL = BILL + "PUKUL : " + waktu + "\n";
                            BILL = BILL + "JENIS TRANSAKSI : " + "\n";
                            BILL = BILL +  jenistransaksi + "\n";
                            BILL = BILL
                                    + "--------------------------------";

                            if (jenistransaksi.equals("Transfer BRI")){

                                BILL = BILL + "\n" + "BANK  : " + bank ;
                                BILL = BILL + "\n" + "NO REKENING : " +" ( "+ kodebank +" ) " + rektujuan;
                                BILL = BILL + "\n" + "NAMA PENERIMA : " + penerima;
                                BILL = BILL + "\n" + "NOMINAL : " + nominal;

                            }else if (jenistransaksi.equals("Transfer Bank Lain")){

                                BILL = BILL + "\n" + "BANK  : " + bank ;
                                BILL = BILL + "\n" + "NO REKENING : " +" ( "+ kodebank +" ) " + rektujuan;
                                BILL = BILL + "\n" + "NAMA PENERIMA : " + penerima;
                                BILL = BILL + "\n" + "NOMINAL : " + nominal;

                            }else if(jenistransaksi.equals("Tarik Tunai")){

                                BILL = BILL + "\n" + "BANK  : " + bank ;
                                BILL = BILL + "\n" + "NO REKENING : " +" ( "+ kodebank +" ) " + rektujuan;
                                BILL = BILL + "\n" + "NAMA PENERIMA : " + penerima;
                                BILL = BILL + "\n" + "NOMINAL : " + nominal;

                            }else if (jenistransaksi.equals("Pulsa & Paket Data")){
                                BILL = BILL + "\n" + "NO TELPON : " + rektujuan;
                                BILL = BILL + "\n" + "NOMINAL : " + nominal;

                            }else if(jenistransaksi.equals("BPJS Kesehatan")){
                                BILL = BILL + "\n" + "NO ID : " + rektujuan;

                            }else if(jenistransaksi.equals("PLN")){
                                BILL = BILL + "\n" + "BANK  : " + bank ;
                                BILL = BILL + "\n" + "NO REKENING : " +" ( "+ kodebank +" ) " + rektujuan;
                                BILL = BILL + "\n" + "NAMA PENERIMA : " + penerima;
                                BILL = BILL + "\n" + "NOMINAL : " + nominal;

                            }else if (jenistransaksi.equals("Cicilan")){
                                BILL = BILL + "\n" + "BANK  : " + bank ;
                                BILL = BILL + "\n" + "NO REKENING : " +" ( "+ kodebank +" ) " + rektujuan;
                                BILL = BILL + "\n" + "NAMA PENERIMA : " + penerima;
                                BILL = BILL + "\n" + "NOMINAL : " + nominal;

                            }else if (jenistransaksi.equals("Transaksi Lainnya")){

                                BILL = BILL + "\n" + "BANK  : " + bank ;
                                BILL = BILL + "\n" + "NO REKENING : " +" ( "+ kodebank +" ) " + rektujuan;
                                BILL = BILL + "\n" + "NAMA PENERIMA : " + penerima;
                                BILL = BILL + "\n" + "NOMINAL : " + nominal;

                            }


                            BILL = BILL
                                    + "\n--------------------------------";
                            BILL = BILL + "\n\n";

                            BILL = BILL + "BIAYA ADMINISTRASI :" + "\n";
                            BILL = BILL + tarif + "\n";

                            BILL = BILL + "TOTAL PEMBAYARAN :" + "\n";
                            BILL = BILL + totalbayar + "\n";

                            BILL = BILL
                                    + "================================\n";
                            BILL = BILL + "\n\n ";
                            os.write(BILL.getBytes());
                            //This is printer specific code you can comment ==== > Start

                            // Setting height
                            int gs = 29;
                            os.write(intToByteArray(gs));
                            int h = 104;
                            os.write(intToByteArray(h));
                            int n = 162;
                            os.write(intToByteArray(n));

                            // Setting Width
                            int gs_width = 29;
                            os.write(intToByteArray(gs_width));
                            int w = 119;
                            os.write(intToByteArray(w));
                            int n_width = 2;
                            os.write(intToByteArray(n_width));


                        } catch (Exception e) {
                            Log.e("MainActivity", "Exe ", e);
                        }
                    }
                };
                t.start();
            }
        });

        mDisc = (Button) findViewById(R.id.dis);
        mDisc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                if (mBluetoothAdapter != null)
                    mBluetoothAdapter.disable();
            }
        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(PrintActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(PrintActivity.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }
    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(PrintActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }
}
