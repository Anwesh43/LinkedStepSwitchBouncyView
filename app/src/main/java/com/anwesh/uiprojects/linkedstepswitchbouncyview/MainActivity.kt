package com.anwesh.uiprojects.linkedstepswitchbouncyview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.stepswitchbouncyview.StepSwitchBouncyView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StepSwitchBouncyView.create(this)
    }
}
