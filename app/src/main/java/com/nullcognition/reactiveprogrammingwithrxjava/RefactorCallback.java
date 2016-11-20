package com.nullcognition.reactiveprogrammingwithrxjava;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import rx.Observable;
import rx.functions.Action0;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;

/**
 * Created by mms on 11/19/16.
 *
 * callback-based API into RxJava with all the benefits such as controlling threads, lifecycle, and
 * cleanup.
 */

public class RefactorCallback {

  final PublishSubject<Integer> ps = PublishSubject.create();

  public RefactorCallback() {
    safeUnsub();
    publishSub();
    ps.subscribe(); // thus it can have multiple subscribers and when the button is pushed, all subs
    // will get the event
    // Subjects, onError will drop and swallow any onErrors after the first
  }

  private void publishSub() {
    Button b = new Button(new Activity());
    b.setOnClickListener(view -> {
      ps.onNext(view.getId());
    });

    // above is the subject object in its total with both thesubject and the onclick listener
    // below is the call
  }

  Observable<Integer> sub() {
    return ps;
  }

  private void safeUnsub() {

    Button b = new Button(new Activity());
    b.setOnClickListener(view -> {
    });

    // instead of create more callbacks that chain on top of each other, we:
  }

  Observable<View> obs() {
    return Observable.create(sub -> {
      Button b = new Button(new Activity());
      b.setOnClickListener(view -> {
        if (sub.isUnsubscribed()) {
          // stream.shutdownAndCleanUpResources();
        } else {
          sub.onNext(view);
        }
        // or can be replaced with a method reference, lisnter(sub::onNext)

        // then if there was another callback you would put the
        // sub.onError(valFromOtherCallback);
      });
      sub.add(Subscriptions.create(new Action0() {
        @Override public void call() {
          Integer i = 9; // some external object
          i.intValue(); // some external method on the object will trigger a shutdowns
          // or we wrap the subscriber onNext with isUnsubscribed(
          // both in the positive and negative code path case.
        }
      }));
    });
  }
}
