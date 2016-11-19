package com.nullcognition.reactiveprogrammingwithrxjava;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.observables.AsyncOnSubscribe;
import rx.subscriptions.Subscriptions;

import static com.nullcognition.reactiveprogrammingwithrxjava.MainActivity.longCalculation;
import static com.nullcognition.reactiveprogrammingwithrxjava.MainActivity.p;
import static com.nullcognition.reactiveprogrammingwithrxjava.MainActivity.src;

/**
 * Created by mms on 11/19/16.
 *
 * Hot and cold observable - cold often occurs from create, just , from, range, and upon
 * subscription the
 * side effect occurs, often in create, example database connection is opened. Typically each
 * subscription
 * gets its own data set from the cold observable.
 */

public class Chapter01_02 {

  public Chapter01_02() {

    //manualUnsub();
    //takeUntil();
    //create();
    // exercise implement never, empty and range using only create
    //realNever().subscribe();
    //never().subscribe(); // good
    //realEmpty();
    //empty();
    //realRange().subscribe();
    //range().subscribe(); // good
    //managingMultipleSubscribers().subscribe(); // iss sequential, thus this will run for 3 sec, onComplete
    //managingMultipleSubscribers().subscribe(); // then i will run
    //Observable cached = managingMultipleSubscribers(); // created the observable, with the cache on the end
    // so any subsequent calls will have the cached values, that might have taken a long time to get. Ex db
    //p("subbed11");
    //cached.subscribe();
    //p("subbed11-done");
    //p("subbed22");
    //cached.subscribe();
    //p("subbed22-done");
    // since each value is cached internally, for long running or infinite streams,  their values will continue
    // to be saved thus may cause a memory leak

    //infiniteStreams();
    //cancelingSubscriptions();
    // Todo look into Subscriptions class, do not use explicit threads in create
    // do a non and a serialized example

    //P01.cache();

    //timer().subscribe(MainActivity::p);
    //interval().take(12).subscribe( );
  }

  @RxLogObservable Observable interval() {
    return Observable.interval(100, TimeUnit.MILLISECONDS);
  }

  @RxLogObservable Observable timer() {
    return Observable.timer(2, TimeUnit.SECONDS);
  }

  private Observable cancelingSubscriptions() {
    return Observable.create(subscriber -> {
      Runnable r = () -> {
        try {
          Thread.sleep(8000);
        } catch (InterruptedException e) {
          e.printStackTrace(); // if interrupted, then the
        }
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(5);
          subscriber.onCompleted();
        }
      };
      final Thread thread = new Thread(r);
      thread.start();
      subscriber.add(Subscriptions.create(thread::interrupt));
    });
  }

  private void infiniteStreams() {
    // if object creation for onNext is costly use
    Observable.create(subscriber -> {
      int i = 1;
      while (!subscriber.isUnsubscribed()) subscriber.onNext(i += 5);
    });
  }

  @RxLogObservable private Observable<Object> managingMultipleSubscribers() {

    return Observable.create(subscriber -> {
      p("create");
      subscriber.onNext(longCalculation(1));
      p("1");
      subscriber.onNext(longCalculation(2));
      p("2");
      subscriber.onCompleted();
      p("complete");
    }).cache();
  }

  @RxLogObservable private Observable<Integer> range() {
    return Observable.create(new Observable.OnSubscribe<Integer>() {
      @Override public void call(Subscriber<? super Integer> subscriber) {
        int start = 3;
        int count = 6;
        for (int i = start; i < start + count; i++) {
          subscriber.onNext(i);
        }
        subscriber.onCompleted();
      }
    });
  }

  @RxLogObservable
  private Observable<Integer> realRange() { // last number exclusing 3(1) ,4(2),5(3),6(4),7)5),8(6)
    return Observable.range(3, 6);
  }

  @RxLogObservable private Observable empty() {
    return Observable.create(new Observable.OnSubscribe<Object>() {
      @Override public void call(Subscriber<? super Object> subscriber) {
        subscriber.onCompleted();
      }
    });
  }

  @RxLogObservable private Observable realEmpty() {
    return Observable.empty();
  }

  @RxLogObservable private Observable realNever() {
    return Observable.never();
  }

  @RxLogObservable private Observable never() {
    return Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(Subscriber<? super Void> subscriber) {
      }
    });
  }

  private void create() {
    Observable<Integer> o = Observable.create(new Observable.OnSubscribe<Integer>() {
      @Override public void call(Subscriber<? super Integer> subscriber) {
        subscriber.onNext(1);
        subscriber.onNext(2);
        subscriber.onNext(3);
        subscriber.onCompleted();
      }
    });

    // what is this?
    Observable<Integer> oo = Observable.create(new AsyncOnSubscribe<Object, Integer>() {
      @Override protected Object generateState() {
        return null;
      }

      @Override protected Object next(Object state, long requested,
          Observer<Observable<? extends Integer>> observer) {
        return null;
      }
    });
  }

  private void takeUntil() {
    src().takeUntil(integer -> integer == 4).subscribe(new Subscriber<Integer>() {
      @Override public void onCompleted() {
        p("done");
      }

      @Override public void onError(Throwable e) {

      }

      @Override public void onNext(Integer integer) {
        p(integer);
      }
    });
  }

  private void manualUnsub() {
    Subscriber<Integer> subscriber = new Subscriber<Integer>() {
      @Override public void onCompleted() {
      }

      @Override public void onError(Throwable e) {
      }

      @Override public void onNext(Integer integer) {
        if (integer == 5) unsubscribe();
      }
    };
    src().subscribe(subscriber);
  }
}
