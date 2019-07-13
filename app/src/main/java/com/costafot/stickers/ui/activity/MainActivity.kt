package com.costafot.stickers.ui.activity

import android.app.Activity
import android.content.ActivityNotFoundException
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

        mainViewModel.errorMessage.observe(this, Observer {
            Toasty.error(this, it).show()
        })

        mainViewModel.toastSingleLiveEvent.observe(this, Observer {
            Toasty.error(this, getString(it)).show()
        })

        mainViewModel.launchIntentSingleLiveEvent.observe(this, Observer {
            when (it) {
                is LaunchIntentContainer.Chooser -> {
                    launchIntentToAddPackToChooser(it.identifier, it.packName)
                }
                is LaunchIntentContainer.Specific -> {
                    launchIntentToAddPackToSpecificPackage(it.identifier, it.packName, it.specificPackage)
                }
            }
        })
    }

    // Handle cases either of WhatsApp are set as default app to handle this intent. We still want users to see both options.
    private fun launchIntentToAddPackToChooser(identifier: String, stickerPackName: String) {
        val intent = mainViewModel.getIntentToAddStickerPack(identifier, stickerPackName)
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.add_to_whatsapp)), ADD_PACK)
        } catch (e: ActivityNotFoundException) {
            Toasty.error(this, getString(R.string.add_pack_fail_prompt_update_whatsapp)).show()
        }
    }

    private fun launchIntentToAddPackToSpecificPackage(identifier: String, stickerPackName: String, whatsappPackageName: String) {
        val intent = mainViewModel.getIntentToAddStickerPack(identifier, stickerPackName)
        intent.apply {
            setPackage(whatsappPackageName)
        }
        try {
            startActivityForResult(intent, ADD_PACK)
        } catch (e: ActivityNotFoundException) {
            Toasty.error(this, getString(R.string.add_pack_fail_prompt_update_whatsapp)).show()
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.loadStickers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == ADD_PACK) {
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
                        Toasty.error(this, "Cancelled but no validation error given.").show()
                    }
                } else {
                    Toasty.error(this, getString(R.string.add_pack_fail_prompt_update_whatsapp)).show()
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
        const val EXTRA_STICKER_PACK_ID = "sticker_pack_id"
        const val EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority"
        const val EXTRA_STICKER_PACK_NAME = "sticker_pack_name"

        const val EXTRA_STICKER_PACK_WEBSITE = "sticker_pack_website"
        const val EXTRA_STICKER_PACK_EMAIL = "sticker_pack_email"
        const val EXTRA_STICKER_PACK_PRIVACY_POLICY = "sticker_pack_privacy_policy"
        const val EXTRA_STICKER_PACK_LICENSE_AGREEMENT = "sticker_pack_license_agreement"
        const val EXTRA_STICKER_PACK_TRAY_ICON = "sticker_pack_tray_icon"
        const val EXTRA_SHOW_UP_BUTTON = "show_up_button"
        const val EXTRA_STICKER_PACK_DATA = "sticker_pack"

        const val ADD_PACK = 200

        const val RESULT_STRING_EXTRA = "validation_error"
    }
}