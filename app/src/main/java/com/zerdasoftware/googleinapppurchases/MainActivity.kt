package com.zerdasoftware.googleinapppurchases

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.billingclient.api.*
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val skuList = ArrayList<String>()
        skuList.add("android.test.purchased")

        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                // To be implemented in a later section.
                Toast.makeText(this,"To be implemented in a later section",Toast.LENGTH_SHORT).show()
            }

        var billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        btn_buyNow.setOnClickListener {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                        // The BillingClient is ready. You can query purchases here.
                        //Toast.makeText(this@MainActivity,"The BillingClient is ready",Toast.LENGTH_SHORT).show()
                        println("The BillingClient is ready")
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            //Toast.makeText(this@MainActivity,"BillingResponseCode.OK",Toast.LENGTH_SHORT).show()
                            println("BillingResponseCode.OK")
                            val params = SkuDetailsParams.newBuilder()
                            params.setSkusList(skuList)
                                .setType(BillingClient.SkuType.INAPP)

                            billingClient.querySkuDetailsAsync(params.build()) {
                                billingResult, skuDetailsList ->

                                for (skuDetails in skuDetailsList!!) {
                                    val flowPurchase = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetails)
                                        .build()
                                    val responseCode = billingClient.launchBillingFlow(this@MainActivity,flowPurchase).responseCode
                                }

                            }
                        }else {
                            Toast.makeText(this@MainActivity,"ERROR",Toast.LENGTH_SHORT).show()
                            println("ERROR")
                        }
                    }
                }
                override fun onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                    Toast.makeText(this@MainActivity,"Google Play by calling the startConnection() method",Toast.LENGTH_SHORT).show()
                }
            })

        }
    }


}