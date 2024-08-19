#include "Base.h"
#include <Windows.h>
#include <stdio.h>

#include <titan_hook.h>

#include "jvm/JVM.hpp"
#include "mc/SDK.hpp"

#include "classes/classes.hpp"
#include "jvm/hotspot/utility/jvm_internal.h"
#include "jvm/hotspot/classes/instance_klass.h"
extern "C" JNIIMPORT VMStructEntry * gHotSpotVMStructs;
extern "C" JNIIMPORT VMTypeEntry * gHotSpotVMTypes;
extern "C" JNIIMPORT VMIntConstantEntry * gHotSpotVMIntConstants;
extern "C" JNIIMPORT VMLongConstantEntry * gHotSpotVMLongConstants;
static auto InitGlobalOffsets() -> void {
	/* .\hotspot\src\share\vm\classfile\javaClasses.hpp -> class java_lang_Class : AllStatic */
	const auto java_lang_Class = JVMWrappers::find_type_fields("java_lang_Class");
	if (!java_lang_Class.has_value()) {
		std::cout << "Failed to find java_lang_Class" << std::endl;
	}

	/* java_lang_Class -> _klass_offset */
	global_offsets::klass_offset = *static_cast<jint*>(java_lang_Class.value().get()["_klass_offset"]->address);
}


void Base::Init()
{
	Utils::CreateConsole();
	JVM::get().setup();
	if (SDK::SetUpForge1181ClassLoader("Render thread")) {
		JNI::set_class_loader(SDK::MinecraftClassLoader);
	}

	JVMWrappers::init(gHotSpotVMStructs, gHotSpotVMTypes, gHotSpotVMIntConstants, gHotSpotVMLongConstants);
	InitGlobalOffsets();
	maps::ClassLoader cl;
	cl.object_instance = SDK::MinecraftClassLoader;

	static jclass main_class = 0;
	for (int i = 0; i < sizeof(jar_classes_data) / 8; i++)
	{
		auto	 k = cl.defineClass((uint8_t*)(jar_classes_data[i]), jar_classes_sizes[i]);
		auto instance = java_hotspot::instance_klass::get_instance_class(k);
		if (instance)
		{
			auto name = instance->get_name()->to_string();
			std::cout << "defined [" << i << "] klass :" << name << " " << k << std::endl;
			if (name == "net/moran/loratadine/Loratadine")
			{
				main_class = k;
			}
		}
	}
	auto m = JNI::get_env()->GetMethodID(main_class, "<init>", "()V");
	JNI::get_env()->NewObject(main_class, m);
}

void Base::Shutdown()
{
	JVM::get().shutdown();
}

void Utils::CreateConsole()
{
	FreeConsole();
	if (!AllocConsole())
	{
		char buffer[1024] = { 0 };
		sprintf_s(buffer, "Failed to AllocConsole( ), GetLastError( ) = %d", GetLastError());
		MessageBoxA(HWND_DESKTOP, buffer, "Error", MB_OK);

		return;
	}

	FILE* fp = nullptr;
	freopen_s(&fp, "CONOUT$", "w", stdout);

	*(__acrt_iob_func(1)) = *fp;
	setvbuf(stdout, NULL, _IONBF, 0);
}

void Utils::CloseConsole()
{
	FILE* fp = (__acrt_iob_func(1));
	if (fp != nullptr) {
		fclose(fp);
	}

	if (!FreeConsole())
	{
		char buffer[1024] = { 0 };
		sprintf_s(buffer, "Failed to FreeConsole(), GetLastError() = %d", GetLastError());
		MessageBoxA(HWND_DESKTOP, buffer, "Error", MB_OK);
		return;
	}
}
