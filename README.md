# CryptoWatch

Aplicación de monitoreo inteligente de criptomonedas para Android y Wear OS.

## Descripción

CryptoWatch es una aplicación complementaria que permite monitorear los precios de las criptomonedas en tiempo real, con una aplicación móvil para Android y una aplicación para relojes inteligentes con Wear OS. La aplicación facilita el seguimiento de las fluctuaciones del mercado sin necesidad de consultar constantemente el teléfono, mediante notificaciones y vibraciones diferenciadas en el reloj.

## Características

### Aplicación Móvil (Android)
- Búsqueda de criptomonedas por nombre o símbolo
- Consulta de precios en tiempo real en dólares USD
- Gráficos históricos (24h, 7d, 30d, 1año)
- Análisis de variaciones porcentuales
- Configuración de alertas personalizadas por precio o porcentaje
- Historial de alertas guardadas
- Sincronización con smartwatch

### Aplicación para Wear OS
- Recepción de alertas cuando se cumplen umbrales configurados
- Información del movimiento: nombre, símbolo, precio y porcentaje
- Patrones de vibración diferenciados:
  - Vibración positiva: indica incremento de precio
  - Vibración negativa: indica disminución de precio
- Consulta rápida de criptomonedas favoritas
- Visualización de notificaciones recientes
- Sincronización de configuraciones desde el teléfono

## Tecnologías

- **Android Studio**: Entorno de desarrollo
- **Kotlin**: Lenguaje de programación
- **Jetpack Compose**: Interfaces de usuario
- **Wear OS y Wear Compose**: Interfaz para relojes inteligentes
- **Wearable Data Layer API**: Comunicación teléfono-reloj
- **API de criptomonedas**: Obtención de precios y datos
- **Room Database**: Almacenamiento local de configuraciones e historial
- **WorkManager**: Tareas periódicas de actualización

## Requisitos del Sistema

- Android Studio Arctic Fox o superior
- JDK 17
- Dispositivo con Android 7.0 (API 24) o superior para el móvil
- Reloj con Wear OS 3.0 o superior

## Estructura del Proyecto

```
CryptoWatch/
├── mobile-app/          # Aplicación Android para teléfono
│   ├── src/main/java/com/cryptowatch/mobile/room/
│   │   ├── AlertEntity.kt       # Entidad Room para alertas
│   │   ├── AlertDao.kt          # DAO para operaciones de alertas
│   │   ├── AlertRoomDatabase.kt # Base de datos Room
│   │   ├── AlertRepository.kt   # Repositorio de datos
│   │   └── AlertViewModel.kt    # ViewModel para AlertsScreen
│   ├── src/main/java/com/cryptowatch/mobile/util/
│   ├── src/main/compose/
│   │   ├── components/          # Componentes reutilizables (AlertCard, CryptoCard)
│   │   └── screens/             # Pantallas (Home, Search, Alerts)
│   └── build.gradle
├── watch-app/           # Aplicación Wear OS para smartwatch
│   ├── src/main/java/com/cryptowatch/watch/util/
│   │   └── VibrationPattern.kt  # Patrones de vibración
│   ├── src/main/compose/
│   │   └── screens/             # Pantallas (Favorites, QuickPrice)
│   └── build.gradle
├── gradle.properties
├── local.properties
└── settings.gradle
```

## Cómo Ejecutar

1. Abrir el proyecto en Android Studio
2. Sincronizar gradle con `sync now`
3. Ejecutar `mobile-app` en un dispositivo Android o emulador
4. Ejecutar `watch-app` en un dispositivo Wear OS o emulador
5. Emparejar los dispositivos mediante Bluetooth y la API de Data Layer

## Permisos Necesarios

- `android.permission.INTERNET`: Para obtener datos de mercado
- `android.permission.ACCESS_NETWORK_STATE`: Para verificar conectividad
- `android.permission.VIBRATE`: Para patrones de vibración en el reloj