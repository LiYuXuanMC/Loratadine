package net.moran.loratadine.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import net.minecraftforge.eventbus.api.IEventBus;
import net.moran.loratadine.event.annotations.EventPriority;

public class EventManager {
   IEventBus EVENT_BUS;
   private final Map<Method, Class<?>> registeredMethodMap;
   private final Map<Method, Object> methodObjectMap;
   private final Map<Class<? extends Event>, List<Method>> priorityMethodMap;

   public EventManager(IEventBus EVENT_BUS) {
      this.EVENT_BUS = EVENT_BUS;
      this.registeredMethodMap = new ConcurrentHashMap<>();
      this.methodObjectMap = new ConcurrentHashMap<>();
      this.priorityMethodMap = new ConcurrentHashMap<>();
   }

   public void register(Object... obj) {
      for (Object object : obj) {
         this.register(object);
      }
   }

   public void register(Object obj) {
      this.EVENT_BUS.register(obj);
      Class<?> clazz = obj.getClass();
      Method[] methods = clazz.getDeclaredMethods();

      for (Method method : methods) {
         Annotation[] annotations = method.getDeclaredAnnotations();

         for (Annotation annotation : annotations) {
            if (annotation.annotationType() == EventPriority.class && method.getParameterTypes().length == 1) {
               this.registeredMethodMap.put(method, method.getParameterTypes()[0]);
               this.methodObjectMap.put(method, obj);
               Class<? extends Event> eventClass = method.getParameterTypes()[0].asSubclass(Event.class);
               this.priorityMethodMap.computeIfAbsent(eventClass, new Function<Class<? extends Event>, List<Method>>() {
                  public List<Method> apply(Class<? extends Event> k) {
                     return new CopyOnWriteArrayList<>();
                  }
               }).add(method);
            }
         }
      }
   }

   public void unregister(Object obj) {
      this.EVENT_BUS.unregister(obj);
      Class<?> clazz = obj.getClass();
      Method[] methods = clazz.getDeclaredMethods();

      for (Method method : methods) {
         if (this.registeredMethodMap.containsKey(method)) {
            this.registeredMethodMap.remove(method);
            this.methodObjectMap.remove(method);
            Class<? extends Event> eventClass = method.getParameterTypes()[0].asSubclass(Event.class);
            List<Method> priorityMethods = this.priorityMethodMap.get(eventClass);
            if (priorityMethods != null) {
               priorityMethods.remove(method);
            }
         }
      }
   }

   public Event call(Event event) {
      Class<? extends Event> eventClass = (Class<? extends Event>)event.getClass();
      List<Method> methods = this.priorityMethodMap.get(eventClass);
      if (methods != null) {
         methods.sort(new Comparator<Method>() {
            public int compare(Method method1, Method method2) {
               EventPriority priority1 = method1.getAnnotation(EventPriority.class);
               EventPriority priority2 = method2.getAnnotation(EventPriority.class);
               int value1 = priority1 != null ? priority1.value() : 10;
               int value2 = priority2 != null ? priority2.value() : 10;
               return Integer.compare(value1, value2);
            }
         });

         for (Method method : methods) {
            Object obj = this.methodObjectMap.get(method);
            method.setAccessible(true);

            try {
               method.invoke(obj, event);
            } catch (Exception var8) {
               var8.printStackTrace();
            }
         }
      }

      return event;
   }
}
