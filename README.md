# MedsTime

<img src="materials/meds_time_icon.png" style="border-radius: 50px; width: 100px;" />

## Описание

Цель приложения MedsTime - помочь пользователям контролировать количество полученных препаратов в аптеке и обеспечить им напоминания о принятии лекарств. Готовое приложение будет предоставлять возможность отслеживать запасы лекарств, напоминать о необходимости их приема, и предоставлять обзор истории потребления.

### Основной стек
- **Язык разработки:** Kotlin;
- **Архитектура:** Clean architecture с паттернами MVI и MVVM;
- **User interface:** XML (70%), Jetpack Compose (30%);
- **Dependency injection:** Koin;
- **Другой стек:** BroadcastReciever, Service, LiveData, Flow, Room, coroutines.

### Упрощенная визуализация общей архитектуры

![](materials/architecture.png "Упрощенная визуализация общей архитектуры")

## Скриншоты
<div style="display: flex; flex-wrap: wrap; justify-content: space-between; align-items: flex-start;">
  <figure>
    <img src="materials/day_add_med.png" style="border-radius: 10px; width: 360px;" />
  </figure>

  <figure>
    <img src="materials/day_intakes_placeholder.png" style="border-radius: 10px; width: 360px;" />
  </figure>
</div>
  
<div style="display: flex; flex-wrap: wrap; justify-content: space-between; align-items: flex-start;">
<figure>
    <img src="materials/day_add_med_2.png" style="border-radius: 10px; width: 360px;" />
  </figure>

<figure>
  <img src="materials/night_add_med.png" style="border-radius: 10px; width: 360px;" />
</figure>
</div>

<div style="display: flex; flex-wrap: wrap; justify-content: space-between; align-items: flex-start;">
<figure>
  <img src="materials/day_intakes.png" style="border-radius: 10px; width: 360px;" />
</figure>

<figure>
  <img src="materials/night_add_med_2.png" style="border-radius: 10px; width: 360px;" />
</figure>
</div>

<div style="display: flex; flex-wrap: wrap; justify-content: space-between; align-items: flex-start;">
<figure>
  <img src="materials/day_intakes_expanded.png" style="border-radius: 10px; width: 360px;" />
</figure>

<figure>
  <img src="materials/night_intakes_expanded.png" style="border-radius: 10px; width: 360px;" />
</figure>
</div>


