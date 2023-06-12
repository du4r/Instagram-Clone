package com.du4r.instagramclone.profile.presentation

import com.du4r.instagramclone.common.base.RequestCallback
import com.du4r.instagramclone.common.model.DataBase
import com.du4r.instagramclone.common.model.Post
import com.du4r.instagramclone.common.model.UserAuth
import com.du4r.instagramclone.profile.Profile
import com.du4r.instagramclone.profile.data.ProfileRepository

class ProfilePresenter(
   private var view: Profile.View?,
   private val repository: ProfileRepository
): Profile.Presenter {

    override fun fetchUserProfile(uuid: String?) {
        view?.showProgress(true)

        repository.fetchUserProfile(uuid, object : RequestCallback<Pair<UserAuth,Boolean?>>{
            override fun onSuccess(data: Pair<UserAuth,Boolean?>) {
                view?.displayUserProfile(data)
            }

            override fun onFailure(message: String) {
                view?.displayRequestFailure(message)
            }

            override fun onComplete() {}
        })

    }

    override fun clear() {
        repository.clearCache()
    }

    override fun fetchUserPosts(uuid: String?) {
        repository.fetchUserPosts(uuid, object: RequestCallback<List<Post>>{
            override fun onSuccess(data: List<Post>) {
               if (data.isEmpty()){
                   view?.displayEmptyPosts()
               }else{
                   view?.displayFullPosts(data)
               }
            }

            override fun onFailure(message: String) {
                view?.displayRequestFailure(message)
            }

            override fun onComplete() {
                view?.showProgress(false)
            }
        })
    }

    override fun followUser(uuid: String?, follow: Boolean) {
        repository.followUser(uuid,follow,object : RequestCallback<Boolean>{
            override fun onSuccess(data: Boolean) {
            }

            override fun onFailure(message: String) {
            }

            override fun onComplete() {
            }
        })
    }

    override fun onDestroy() {
        view = null
    }

}