package com.feelsokman.analytics

interface Analytics {

    fun trackShare(sound: String)
    fun trackUpvote()
    fun trackDownvote()
    fun trackDownload()
    fun trackAbout()
    fun trackThemeChange()
    fun trackTutorial()
    fun trackStoragePermDenied()
    fun trackRedirectRating()
    fun trackStopAllSounds()
    fun trackMaxGifsDialog()
    fun trackSoundChoiceDialog()
    fun trackFailureSaveFile()
    fun trackSettingPermDenied()
    fun trackAudioPermDenied()
    fun trackAudioPermNeverAskAgain()
    fun trackPermDialogDenied()
    fun trackShareAppLink()
    fun trackCredits()
    fun trackPrivacyPolicy()
    fun trackDevPageRedirect()
    fun trackSongSelectionDialog()
    fun trackNoAvailableSongsError()
    fun trackSongOptionsDialog()
    fun trackOptionsFragment()
    fun trackSoundPlayed(sound: String)
    fun trackRemoveViews()
    fun trackMainMusicToggle()
    fun trackChangeMusic(sound: String)
    fun trackChangeRingTone()
    fun trackChangeNotificationSound()
    fun trackUpdateMaxGifs(number: Int)
    fun trackBottomSheetButtonHint()
    fun trackRingtoneError()
    fun trackErrorNotification()
    fun trackShareGif(gif: String)
    fun trackTutorialGang()
    fun trackCopyToClipboard(gif: String)
    fun trackException(ex: String)
    fun trackFavouriteChoicesDialog()
    fun trackFavourite(fav: String)
    fun trackTabAll()
    fun trackTabFavourites()
    fun trackTabSearch()
    fun trackSaveSound(sound: String)
}