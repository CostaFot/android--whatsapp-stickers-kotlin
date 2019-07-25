package com.costafot.stickers.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.costafot.stickers.BuildConfig
import com.costafot.stickers.R
import com.costafot.stickers.model.ToastMessage
import com.costafot.stickers.ui.activity.viewmodel.MainViewModel
import com.costafot.stickers.ui.activity.viewmodel.MainViewModelFactory
import com.costafot.stickers.ui.base.BaseActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    internal lateinit var factoryMainViewModel: MainViewModelFactory

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, factoryMainViewModel).get(MainViewModel::class.java)
    }
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()

        mainViewModel.toastSingleLiveEvent.observe(this, Observer(this@MainActivity::showErrorToast))
        mainViewModel.launchIntentSingleLiveEvent.observe(this, Observer(this@MainActivity::handleLaunchIntentCommand))
    }

    private fun handleLaunchIntentCommand(launchIntentCommand: LaunchIntentCommand) {
        when (launchIntentCommand) {
            is LaunchIntentCommand.Chooser -> {
                launchIntentToAddPackToChooser(launchIntentCommand.intent)
            }
            is LaunchIntentCommand.Specific -> {
                launchIntentToAddPackToSpecificPackage(launchIntentCommand.intent)
            }
        }
    }

    private fun showErrorToast(toastMessage: ToastMessage) {
        when (toastMessage.hasResource()) {
            true -> Toasty.error(this, getString(toastMessage.resourceId)).show()
            else -> Toasty.error(this, toastMessage.message.toString()).show()
        }
    }

    // Handle cases either of WhatsApp are set as default app to handle this intent. We still want users to see both options.
    private fun launchIntentToAddPackToChooser(intent: Intent) {
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.add_to_whatsapp)), REQUEST_CODE_ADD_PACK)
        } catch (e: Throwable) {
            showErrorToast(ToastMessage(resourceId = R.string.add_pack_fail_prompt_update_whatsapp))
        }
    }

    private fun launchIntentToAddPackToSpecificPackage(intent: Intent) {
        try {
            startActivityForResult(intent, REQUEST_CODE_ADD_PACK)
        } catch (e: Throwable) {
            showErrorToast(ToastMessage(resourceId = R.string.add_pack_fail_prompt_update_whatsapp))
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.loadStickers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_CODE_ADD_PACK) {
            if (resultCode == Activity.RESULT_CANCELED) {
                if (intent != null) {
                    val bundle = intent.extras
                    if (bundle != null && bundle.containsKey(RESULT_STRING_EXTRA)) {
                        val validationError: String? = bundle.getString(RESULT_STRING_EXTRA)
                        if (validationError != null) {
                            if (BuildConfig.DEBUG) {
                                Toasty.error(this, validationError).show()
                            }
                            Timber.e("Validation failed:$validationError")
                        }
                    } else {
                        showErrorToast(ToastMessage(message = "Cancelled but no validation error given."))
                    }
                } else {
                    showErrorToast(ToastMessage(resourceId = R.string.add_pack_fail_prompt_update_whatsapp))
                }
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        setupDestinationListener(navController)
    }

    private fun setupDestinationListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.hostFragment -> Timber.tag("NavigationLogger").d("hostFragment showing!")
                R.id.anotherFragment -> Timber.tag("NavigationLogger").d("anotherFragment showing!")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    companion object {
        const val REQUEST_CODE_ADD_PACK = 200
        const val RESULT_STRING_EXTRA = "validation_error"
    }
}