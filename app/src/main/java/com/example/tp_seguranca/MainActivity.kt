package com.example.tp_seguranca

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.time.Instant
import java.util.*


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult (
        requestCode: Int ,
        permissions: Array<String> ,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSIONS_CODE -> {
                var i = 0
                while (i < permissions. size ) {
                    if (permissions[i]. equals (
                            Manifest.permission. ACCESS_FINE_LOCATION ,
                            ignoreCase = true )
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED
                    ) {
                        readMyCurrentCoordinates()
                    } else if (permissions[i]. equals (
                            Manifest.permission. WRITE_EXTERNAL_STORAGE ,
                            ignoreCase = true )
                        && grantResults[i] == PackageManager. PERMISSION_GRANTED
                    ) {
                        createDeleteFile(latitude = "",longitude = "")
                    } else if (permissions[i]. equals (
                            Manifest.permission. READ_EXTERNAL_STORAGE ,
                            ignoreCase = true )
                        && grantResults[i] == PackageManager. PERMISSION_GRANTED
                    ) {
                        readFile()
                    }
                    i++
                }
            }
        }
        super .onRequestPermissionsResult(
            requestCode , permissions , grantResults)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_listagem.setOnClickListener {
            startActivity(Intent(this,ListagemActivity::class.java))
        }
    }
    private fun readMyCurrentCoordinates () {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE ) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER )
        val isNetworkEnabled = locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER )
        if (!isGPSEnabled && !isNetworkEnabled) {
            Log.d( "Permissao" , "Ative os serviços necessários" )
        } else {
            if (isGPSEnabled) {
                try {
                    locationManager.requestSingleUpdate(
                        LocationManager.GPS_PROVIDER
                       , locationListener,null )
                } catch (ex: SecurityException) {
                    Log.d( "Permissao" , "Security Exception" )
                }
            }
            else if (isNetworkEnabled) {
                try {
                    locationManager.requestSingleUpdate(
                        LocationManager. NETWORK_PROVIDER
                        , locationListener,null )
                } catch (ex: SecurityException) {
                    Log.d( "Permissao" , "Security Exception" )
                }
            }
        }
    }

    private val locationListener : LocationListener =
        object : LocationListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onLocationChanged (location: Location)

            {
                var longitude = location.longitude.toString()
                var latitude = location.latitude.toString()
                Toast.makeText( applicationContext ,
                    "Localizacao salva com sucesso" ,
                    Toast. LENGTH_SHORT ).show()
                createDeleteFile(latitude,longitude)
            }
            override fun onStatusChanged (
                provider: String , status: Int , extras: Bundle) {}
            override fun onProviderEnabled (provider: String) {}
            override fun onProviderDisabled (provider: String) {}
        }

    private fun callDialog (mensagem: String ,
                            permissions: Array<String>) {
        var mDialog = AlertDialog.Builder( this )
            .setTitle( "Permissão" ).setMessage(mensagem)
            .setPositiveButton( "Ok" )
            { dialog , id ->
                ActivityCompat.requestPermissions(
                    this@MainActivity , permissions ,
                    REQUEST_PERMISSIONS_CODE )
                dialog.dismiss()
            }
            .setNegativeButton( "Cancel" )
            { dialog , id ->
                dialog.dismiss()
            }
        mDialog.show()
    }

    val REQUEST_PERMISSIONS_CODE = 128

    @RequiresApi(Build.VERSION_CODES.O)

private fun createDeleteFile (latitude:String,longitude:String)  {

    var dataehr = Date.from(Instant.now())
    dataehr.toString()
    val file = File(getExternalFilesDir( null ) , "$dataehr.crd" )
    if (file.exists()){
        file.delete()
    }
    else {
        try {
            val os: OutputStream = FileOutputStream(file)

            os.write("latitude: $latitude , longitude : $longitude".toByteArray())
            os.close()

        }
        catch (e: IOException) {
            Log.d( "Permissao" , "Erro de escrita em arquivo" )
        }
    }
    return
}

@RequiresApi(Build.VERSION_CODES.KITKAT)
private fun readFile () {
    val file = File(getExternalFilesDir(null),"teste")
    if (!file.exists()) {
        Toast.makeText( this@MainActivity ,
            "Arquivos não encontrados" ,
            Toast. LENGTH_SHORT ).show()
        return
    }
    val text = file.name
    //try {

//            val br = BufferedReader(FileReader(file))
//            var line: String?
//            while (br.readLine().also { line = it } != null ) {
//                text.append(line)
//                text.append('\n')
// }
//            br.close()
//        } catch (e: IOException) {
//            Log.d( "Permissao" , "Erro de leitura no arquivo" )
//        }
    Toast.makeText( this@MainActivity ,
        text.toString() ,
        Toast. LENGTH_SHORT ).show()
}



fun callAccessLocation (view: View?) {
    val permissionAFL = ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )
    val permissionACL = ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION )
    if (permissionAFL != PackageManager.PERMISSION_GRANTED &&
        permissionACL != PackageManager.PERMISSION_GRANTED ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION )) {
            callDialog( "É preciso liberar ACCESS_FINE_LOCATION" ,
                arrayOf (Manifest.permission. ACCESS_FINE_LOCATION ))
        } else {
            ActivityCompat.requestPermissions( this,
                arrayOf (Manifest.permission.ACCESS_FINE_LOCATION ) ,
                REQUEST_PERMISSIONS_CODE )
        }
    } else {

//            var dataehr = Date.from(Instant.now())
//            Toast.makeText(this,"$dataehr",Toast.LENGTH_SHORT).show()
        readMyCurrentCoordinates()
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun callWriteOnSDCard(view: View) {
    if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager. PERMISSION_GRANTED ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission. WRITE_EXTERNAL_STORAGE
            )) {
            callDialog(
                "É preciso liberar WRITE_EXTERNAL_STORAGE" ,
                arrayOf (Manifest.permission. WRITE_EXTERNAL_STORAGE )
            )
        } else {
            ActivityCompat.requestPermissions( this,
                arrayOf (Manifest.permission. WRITE_EXTERNAL_STORAGE ) ,
                REQUEST_PERMISSIONS_CODE )
        }
    } else {
        createDeleteFile(longitude = "",latitude = "")
    }
}
@RequiresApi(Build.VERSION_CODES.KITKAT)
fun callReadFromSDCard(view: View) {
    if (ContextCompat.checkSelfPermission(
            this, Manifest.permission. READ_EXTERNAL_STORAGE
        ) != PackageManager. PERMISSION_GRANTED ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission. READ_EXTERNAL_STORAGE
            )) {
            callDialog(
                "É preciso a liberar READ_EXTERNAL_STORAGE" ,
                arrayOf (Manifest.permission. READ_EXTERNAL_STORAGE )
            )
        } else {
            ActivityCompat.requestPermissions( this,
                arrayOf (Manifest.permission. READ_EXTERNAL_STORAGE ) ,
                REQUEST_PERMISSIONS_CODE )
        }
    } else {
        readFile()
    }

}

}