package dk.eleknet.tattoodemo.app

import android.os.Bundle
import dk.eleknet.tattoodemo.R
import dk.eleknet.tattoodemo.app.grid.GridFragment
import dk.eleknet.tattoodemo.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridFragment = GridFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.navigationFrame, gridFragment)
            .commit()
    }
}
