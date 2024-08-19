// dllmain.cpp : 定义 DLL 应用程序的入口点。
#include <Windows.h>
#include "base/Base.h"

static DWORD WINAPI BootStrapThread(LPVOID hDll)
{
	//Setup Client Here
	Base::Init();
	return NULL;
}
BOOL APIENTRY DllMain(HMODULE hModule,
	DWORD  ul_reason_for_call,
	LPVOID lpReserved
)
{
	switch (ul_reason_for_call)
	{
	case DLL_PROCESS_ATTACH:
		DisableThreadLibraryCalls(hModule);
		CreateThread(0, 0, BootStrapThread, 0, 0, 0);
		break;
	case DLL_THREAD_ATTACH:
		break;
	case DLL_THREAD_DETACH:
	case DLL_PROCESS_DETACH:
		break;
	}
	return TRUE;
}

