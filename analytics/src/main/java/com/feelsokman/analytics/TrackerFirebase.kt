package com.feelsokman.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class TrackerFirebase(private val firebaseAnalytics: FirebaseAnalytics) : Analytics {
    override fun trackSaveSound(sound: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, sound)
        firebaseAnalytics.logEvent(TrackingEvents.SAVE_SOUND.name.toLowerCase(), params)
    }

    override fun trackTabAll() {
        firebaseAnalytics.logEvent(TrackingEvents.TAB_ALL.name.toLowerCase(), null)
    }

    override fun trackTabFavourites() {
        firebaseAnalytics.logEvent(TrackingEvents.TAB_FAVOURITES.name.toLowerCase(), null)
    }

    override fun trackTabSearch() {
        firebaseAnalytics.logEvent(TrackingEvents.TAB_SEARCH.name.toLowerCase(), null)
    }

    override fun trackFavourite(fav: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, fav)
        firebaseAnalytics.logEvent(TrackingEvents.FAVOURITE.name.toLowerCase(), params)
    }

    override fun trackFavouriteChoicesDialog() {
        firebaseAnalytics.logEvent(TrackingEvents.FAV_CHOICE_DIALOG.name.toLowerCase(), null)
    }

    override fun trackException(ex: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, ex)
        firebaseAnalytics.logEvent(TrackingEvents.EXCEPTION.name.toLowerCase(), params)
    }

    override fun trackCopyToClipboard(gif: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, gif)
        firebaseAnalytics.logEvent(TrackingEvents.COPY_TO_CLIP.name.toLowerCase(), params)
    }

    override fun trackTutorialGang() {
        firebaseAnalytics.logEvent(TrackingEvents.TUTORIAL_GANG.name.toLowerCase(), null)
    }

    override fun trackShareGif(gif: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, gif)
        firebaseAnalytics.logEvent(TrackingEvents.SHARE_GIF.name.toLowerCase(), params)
    }

    override fun trackErrorNotification() {
        firebaseAnalytics.logEvent(TrackingEvents.ERROR_NOTIFICATION.name.toLowerCase(), null)
    }

    override fun trackRingtoneError() {
        firebaseAnalytics.logEvent(TrackingEvents.ERROR_RINGTONE.name.toLowerCase(), null)
    }

    override fun trackBottomSheetButtonHint() {
        firebaseAnalytics.logEvent(TrackingEvents.BOTTOM_SHEET_HINT.name.toLowerCase(), null)
    }

    override fun trackUpdateMaxGifs(number: Int) {
        val params = Bundle()
        params.putInt(FirebaseAnalytics.Param.ITEM_NAME, number)
        firebaseAnalytics.logEvent(TrackingEvents.UPDATE_MAX_GIFS.name.toLowerCase(), params)
    }

    override fun trackChangeNotificationSound() {
        firebaseAnalytics.logEvent(TrackingEvents.NOTIFICATION.name.toLowerCase(), null)
    }

    override fun trackChangeRingTone() {
        firebaseAnalytics.logEvent(TrackingEvents.RINGTONE.name.toLowerCase(), null)
    }

    override fun trackChangeMusic(sound: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, sound)
        firebaseAnalytics.logEvent(TrackingEvents.CHANGE_MUSIC.name.toLowerCase(), params)
    }

    override fun trackMainMusicToggle() {
        firebaseAnalytics.logEvent(TrackingEvents.MAIN_MUSIC_TOGGLE.name.toLowerCase(), null)
    }

    override fun trackRemoveViews() {
        firebaseAnalytics.logEvent(TrackingEvents.CLEAN_SCREEN.name.toLowerCase(), null)
    }

    override fun trackSoundPlayed(sound: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, sound)
        firebaseAnalytics.logEvent(TrackingEvents.SOUND.name.toLowerCase(), params)
    }

    override fun trackOptionsFragment() {
        firebaseAnalytics.logEvent(TrackingEvents.OPTIONS_FRAG.name.toLowerCase(), null)
    }

    override fun trackSongOptionsDialog() {
        firebaseAnalytics.logEvent(TrackingEvents.SONG_OPTIONS.name.toLowerCase(), null)
    }

    override fun trackNoAvailableSongsError() {
        firebaseAnalytics.logEvent(TrackingEvents.NO_SONGS_ERROR.name.toLowerCase(), null)
    }

    override fun trackSongSelectionDialog() {
        firebaseAnalytics.logEvent(TrackingEvents.SHOW_SONG_SELECTION.name.toLowerCase(), null)
    }

    override fun trackDevPageRedirect() {
        firebaseAnalytics.logEvent(TrackingEvents.DEV_PAGE_REDIRECT.name.toLowerCase(), null)
    }

    override fun trackPrivacyPolicy() {
        firebaseAnalytics.logEvent(TrackingEvents.PRIVACY_POLICY.name.toLowerCase(), null)
    }

    override fun trackCredits() {
        firebaseAnalytics.logEvent(TrackingEvents.CREDITS.name.toLowerCase(), null)
    }

    override fun trackShareAppLink() {
        firebaseAnalytics.logEvent(TrackingEvents.SHARE_APP_LINK.name.toLowerCase(), null)
    }

    override fun trackPermDialogDenied() {
        firebaseAnalytics.logEvent(TrackingEvents.PERM_DIALOG_FAIL.name.toLowerCase(), null)
    }

    override fun trackAudioPermNeverAskAgain() {
        firebaseAnalytics.logEvent(TrackingEvents.AUDIO_PERM_NEVER_ASK.name.toLowerCase(), null)
    }

    override fun trackAudioPermDenied() {
        firebaseAnalytics.logEvent(TrackingEvents.AUDIO_PERM_FAIL.name.toLowerCase(), null)
    }

    override fun trackSettingPermDenied() {
        firebaseAnalytics.logEvent(TrackingEvents.SETTINGS_PERM_FAIL.name.toLowerCase(), null)
    }

    override fun trackFailureSaveFile() {
        firebaseAnalytics.logEvent(TrackingEvents.SAVE_FAIL.name.toLowerCase(), null)
    }

    override fun trackSoundChoiceDialog() {
        firebaseAnalytics.logEvent(TrackingEvents.SOUND_CHOICE.name.toLowerCase(), null)
    }

    override fun trackMaxGifsDialog() {
        firebaseAnalytics.logEvent(TrackingEvents.MAX_GIFS.name.toLowerCase(), null)
    }

    override fun trackStopAllSounds() {
        firebaseAnalytics.logEvent(TrackingEvents.STOP_ALL_SOUNDS.name.toLowerCase(), null)
    }

    override fun trackRedirectRating() {
        firebaseAnalytics.logEvent(TrackingEvents.RATING_REDIRECT.name.toLowerCase(), null)
    }

    override fun trackStoragePermDenied() {
        firebaseAnalytics.logEvent(TrackingEvents.STORE_PERM_DENIED.name.toLowerCase(), null)
    }

    override fun trackDownvote() {
        firebaseAnalytics.logEvent(TrackingEvents.DOWNVOTE.name.toLowerCase(), null)
    }

    override fun trackDownload() {
        firebaseAnalytics.logEvent(TrackingEvents.DOWNLOAD.name.toLowerCase(), null)
    }

    override fun trackAbout() {
        firebaseAnalytics.logEvent(TrackingEvents.ABOUT.name.toLowerCase(), null)
    }

    override fun trackThemeChange() {
        firebaseAnalytics.logEvent(TrackingEvents.THEME_CHANGE.name.toLowerCase(), null)
    }

    override fun trackTutorial() {
        firebaseAnalytics.logEvent(TrackingEvents.TUTORIAL.name.toLowerCase(), null)
    }

    override fun trackUpvote() {
        firebaseAnalytics.logEvent(TrackingEvents.UPVOTE.name.toLowerCase(), null)
    }

    override fun trackShare(sound: String) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, sound)
        firebaseAnalytics.logEvent(TrackingEvents.SHARE.name.toLowerCase(), params)
    }
}