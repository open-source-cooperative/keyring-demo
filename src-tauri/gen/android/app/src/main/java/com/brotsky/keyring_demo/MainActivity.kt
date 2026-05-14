package com.brotsky.keyring_demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import android.content.Context

class MainActivity : TauriActivity() {
  private external fun initNdkContext(context: Context)
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    initNdkContext(this.applicationContext)
  }
}
