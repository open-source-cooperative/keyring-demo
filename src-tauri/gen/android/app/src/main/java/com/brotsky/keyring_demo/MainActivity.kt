package com.brotsky.keyring_demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge

class MainActivity : TauriActivity() {
  private external fun init_android_context(context: Context)
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    init_android_context(this.applicationContext)
  }
}
